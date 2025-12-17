import { useState } from 'react'
import Navbar from '../components/Navbar'
import './DigitalDrawing.css'

function DigitalDrawing() {
  const [selectedCards, setSelectedCards] = useState([])
  const [cardPositions] = useState(() => {
    // Generate random positions for each card once on mount
    return Array.from({ length: 35 }, () => ({
      rotation: Math.random() * 16 - 8, // Random rotation between -8 and 8 degrees
      translateY: Math.random() * 20 - 10, // Random vertical offset between -10px and 10px
      shimmerDelay: Math.random() * 8, // Random delay for shimmer effect between 0-8s
      invertStars: Math.random() > 0.5 // Randomly invert which star is bright
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
        <div className="card-deck">
          {cardPositions.map((position, i) => (
            <div
              key={i}
              className={`tarot-card ${isCardSelected(i) ? 'card-hidden' : ''} ${position.invertStars ? 'invert-stars' : ''}`}
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
