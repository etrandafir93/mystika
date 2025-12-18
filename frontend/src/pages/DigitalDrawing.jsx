import { useState, useEffect } from 'react'
import Navbar from '../components/Navbar'
import './DigitalDrawing.css'

function DigitalDrawing() {
  const [selectedCards, setSelectedCards] = useState([])
  const [isMobile, setIsMobile] = useState(window.innerWidth <= 768)

  useEffect(() => {
    const handleResize = () => {
      setIsMobile(window.innerWidth <= 768)
    }

    window.addEventListener('resize', handleResize)
    return () => window.removeEventListener('resize', handleResize)
  }, [])

  const numberOfCards = isMobile ? 20 : 40

  const [cardPositions] = useState(() => {
    // Generate random positions for each card once on mount
    return Array.from({ length: 40 }, () => ({
      rotation: Math.random() * 16 - 8, // Random rotation between -8 and 8 degrees
      translateY: Math.random() * 20 - 10, // Random vertical offset between -10px and 10px
      shimmerDelay: Math.random() * 8, // Random delay for shimmer effect between 0-8s
      invertStars: Math.random() > 0.5, // Randomly invert which star is bright
      hasShine: Math.random() < 0.3 // 30% chance to have shine effect on hover
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

        <div className="selected-cards-area">
          <div className="selected-cards-container">
            {[0, 1, 2].map((slotIndex) => {
              const cardIndex = selectedCards[slotIndex]
              if (cardIndex !== undefined) {
                const position = cardPositions[cardIndex]
                return (
                  <div
                    key={slotIndex}
                    className={`tarot-card selected-card ${position.invertStars ? 'invert-stars' : ''}`}
                    style={{
                      animationDelay: `${slotIndex * 0.2}s`,
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
                )
              } else {
                return (
                  <div key={slotIndex} className="card-slot-placeholder"></div>
                )
              }
            })}
          </div>
        </div>
      </div>
    </div>
  )
}

export default DigitalDrawing
