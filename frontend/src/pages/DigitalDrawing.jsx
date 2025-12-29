import { useState, useEffect } from 'react'
import Navbar from '../components/Navbar'
import './DigitalDrawing.css'

function DigitalDrawing() {
  const [selectedCards, setSelectedCards] = useState([])
  const [isMobile, setIsMobile] = useState(window.innerWidth <= 768)
  const [spreadData, setSpreadData] = useState(null)
  const [flippedCards, setFlippedCards] = useState([])
  const [zoomedCard, setZoomedCard] = useState(null)

  useEffect(() => {
    const handleResize = () => {
      setIsMobile(window.innerWidth <= 768)
    }

    window.addEventListener('resize', handleResize)
    return () => window.removeEventListener('resize', handleResize)
  }, [])

  useEffect(() => {
    // When 3 cards are selected, create a three-card spread
    if (selectedCards.length === 3 && !spreadData) {
      const createThreeCardSpread = async () => {
        try {
          const drawingId = `drawing-${Date.now()}`
          const response = await fetch('/api/readings', {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json',
            },
            body: JSON.stringify({
              id: drawingId,
              deckSlug: 'rider-waite',
              drawingType: 'THREE_CARD_SPREAD'
            })
          })

          if (response.ok) {
            const data = await response.json()
            console.log('Three-card spread created:', JSON.stringify(data, null, 2))
            setSpreadData(data)
          } else {
            console.error('Failed to create three-card spread:', response.status)
          }
        } catch (error) {
          console.error('Error creating three-card spread:', error)
        }
      }

      createThreeCardSpread()
    }
  }, [selectedCards, spreadData])

  const numberOfCards = isMobile ? 20 : 40

  const [cardPositions] = useState(() => {
    // Generate random positions for each card once on mount
    return Array.from({ length: 40 }, () => ({
      rotation: Math.random() * 16 - 8, // Random rotation between -8 and 8 degrees
      translateY: Math.random() * 20 - 10, // Random vertical offset between -10px and 10px
      shimmerDelay: Math.random() * 8, // Random delay for shimmer effect between 0-8s
      invertStars: Math.random() > 0.5, // Randomly invert which star is bright
      hasShine: Math.random() < 0.3, // 30% chance to have shine effect on hover
      selectedRotation: Math.random() * 8 - 4 // Random rotation between -4 and 4 degrees for selected cards
    }))
  })

  const handleCardClick = (cardIndex) => {
    // Only allow selecting up to 3 cards
    if (selectedCards.length < 3 && !selectedCards.includes(cardIndex)) {
      setSelectedCards([...selectedCards, cardIndex])
    }
  }

  const isCardSelected = (cardIndex) => {
    return selectedCards.includes(cardIndex)
  }

  const playFlipSound = () => {
    const audioContext = new (window.AudioContext || window.webkitAudioContext)()

    // Create multiple oscillators for a richer, mystical sound
    const frequencies = [261.63, 329.63, 392.00] // C4, E4, G4 (C major chord)

    frequencies.forEach((freq, index) => {
      const oscillator = audioContext.createOscillator()
      const gainNode = audioContext.createGain()

      oscillator.type = 'sine' // Smooth, ethereal tone
      oscillator.frequency.setValueAtTime(freq, audioContext.currentTime)

      // Add slight vibrato for mystical effect
      const vibrato = audioContext.createOscillator()
      const vibratoGain = audioContext.createGain()
      vibrato.frequency.value = 5
      vibratoGain.gain.value = 3
      vibrato.connect(vibratoGain)
      vibratoGain.connect(oscillator.frequency)

      oscillator.connect(gainNode)
      gainNode.connect(audioContext.destination)

      // Stagger the notes slightly
      const startTime = audioContext.currentTime + (index * 0.05)
      const duration = 0.8

      gainNode.gain.setValueAtTime(0, startTime)
      gainNode.gain.linearRampToValueAtTime(0.15, startTime + 0.1)
      gainNode.gain.exponentialRampToValueAtTime(0.01, startTime + duration)

      oscillator.start(startTime)
      vibrato.start(startTime)
      oscillator.stop(startTime + duration)
      vibrato.stop(startTime + duration)
    })
  }

  const handleCardFlip = (slotIndex, cardData) => {
    // Only allow flipping if all 3 cards are selected
    if (selectedCards.length < 3) {
      return
    }

    if (!flippedCards.includes(slotIndex)) {
      playFlipSound()
      setFlippedCards([...flippedCards, slotIndex])
    } else {
      // If already flipped, zoom into the card
      setZoomedCard(cardData)
    }
  }

  const handleCloseZoom = () => {
    setZoomedCard(null)
  }

  return (
    <div className="page">
      <Navbar pageTitle="Celestial Reading" />
      <div className="page-content">
        <div className={`card-deck ${selectedCards.length === 3 ? 'collapsed' : ''}`}>
          {cardPositions.slice(0, numberOfCards).map((position, i) => (
            <div
              key={i}
              className={`tarot-card ${isCardSelected(i) ? 'card-hidden' : ''} ${position.invertStars ? 'invert-stars' : ''} ${position.hasShine ? 'has-shine' : ''}`}
              onClick={() => handleCardClick(i)}
              style={{
                animationDelay: `${i * 0.1}s`,
                '--card-rotation': `${position.rotation}deg`,
                '--card-offset-y': `${position.translateY}px`,
                '--shimmer-delay': `${position.shimmerDelay}s`
              }}
            >
              <div className="card-back">
                <div className="card-pattern">
                  <div className="celestial-symbol">✦</div>
                  <div className="moon-phase">☽</div>
                  <div className="star-accent">✧</div>
                </div>
              </div>
            </div>
          ))}
        </div>

        {spreadData?.reading?.chapters && spreadData.reading.chapters.length > 0 && flippedCards.length > 0 && (
          <div className="reading-section">
            <div className="reading-chapters">
              {spreadData.reading.chapters.map((chapter, index) => {
                if (!flippedCards.includes(index)) return null;
                const cardData = spreadData?.drawing?.cards?.[index];
                return (
                  <div
                    key={index}
                    className="reading-chapter"
                    style={{ animationDelay: `${flippedCards.indexOf(index) * 0.15}s` }}
                    onClick={() => cardData && setZoomedCard(cardData)}
                  >
                    <h3 className="chapter-title">{chapter.title}</h3>
                    <p className="chapter-content">{chapter.content}</p>
                  </div>
                );
              })}
            </div>
          </div>
        )}

        <div className="selected-cards-area">
          <div className="selected-cards-container">
            {[0, 1, 2].map((slotIndex) => {
              const cardIndex = selectedCards[slotIndex]
              if (cardIndex !== undefined) {
                const position = cardPositions[cardIndex]
                const isFlipped = flippedCards.includes(slotIndex)
                const cardData = spreadData?.drawing?.cards?.[slotIndex]

                return (
                  <div key={slotIndex} className={`card-wrapper ${isFlipped ? 'flipped' : ''}`} style={{ display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
                    <div
                      className={`tarot-card selected-card ${position.invertStars ? 'invert-stars' : ''} ${isFlipped ? 'flipped' : ''}`}
                      style={{
                        animationDelay: `${slotIndex * 0.2}s`,
                        '--shimmer-delay': `${position.shimmerDelay}s`,
                        '--selected-rotation': `${position.selectedRotation}deg`
                      }}
                      onClick={() => handleCardFlip(slotIndex, cardData)}
                    >
                      <div className="card-inner">
                        <div className="card-back">
                          <div className="card-pattern">
                            <div className="celestial-symbol">✦</div>
                            <div className="moon-phase">☽</div>
                            <div className="star-accent">✧</div>
                          </div>
                        </div>
                        {cardData && (
                          <div className={`card-front ${cardData.orientation === 'REVERSED' ? 'reversed' : ''}`}>
                            <img src={cardData.imageUrl} alt="Tarot card" />
                          </div>
                        )}
                      </div>
                    </div>
                    {cardData && (
                      <>
                        <div className="card-name-label">
                          {cardData.name}
                        </div>
                        {cardData.orientation === 'REVERSED' && (
                          <div className="card-orientation-label">
                            (reversed)
                          </div>
                        )}
                      </>
                    )}
                  </div>
                )
              } else {
                return (
                  <div key={slotIndex} className="card-slot-placeholder"></div>
                )
              }
            })}
          </div>
        </div>

        {zoomedCard && (
          <div className="zoom-overlay" onClick={handleCloseZoom}>
            <button className="zoom-close-button" onClick={handleCloseZoom}>
              ✕
            </button>
            <div className="zoom-content">
              <div className="zoom-card-image">
                <img
                  src={zoomedCard.imageUrl}
                  alt={zoomedCard.name}
                  className={zoomedCard.orientation === 'REVERSED' ? 'reversed' : ''}
                />
              </div>
              <div className="zoom-card-details">
                <div className="zoom-card-header">
                  <div className="zoom-card-name">{zoomedCard.name}</div>
                  {zoomedCard.orientation === 'REVERSED' && (
                    <div className="zoom-card-orientation">(reversed)</div>
                  )}
                </div>
                <div className="zoom-card-meaning">
                  <div className="zoom-section-title">✦ Meaning ✦</div>
                  <div className="zoom-section-content">{zoomedCard.meaning}</div>
                </div>
                <div className="zoom-card-symbols">
                  <div className="zoom-section-title">✧ Symbols ✧</div>
                  <div className="zoom-section-content">
                    {zoomedCard.symbols && zoomedCard.symbols.length > 0 ? (
                      <ul className="symbols-list">
                        {zoomedCard.symbols.map((symbol, index) => (
                          <li key={index}>{symbol}</li>
                        ))}
                      </ul>
                    ) : (
                      <p>No symbols available</p>
                    )}
                  </div>
                </div>
              </div>
            </div>
          </div>
        )}
      </div>
    </div>
  )
}

export default DigitalDrawing
