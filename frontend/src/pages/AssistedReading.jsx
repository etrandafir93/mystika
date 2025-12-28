import { useState, useEffect } from 'react'
import Navbar from '../components/Navbar'
import './AssistedReading.css'

function AssistedReading() {
  const [deck, setDeck] = useState(null)
  const [selectedCard, setSelectedCard] = useState(null)
  const [cardRotation, setCardRotation] = useState(0)
  const [totalRotation, setTotalRotation] = useState(0)

  useEffect(() => {
    // Fetch the deck data
    const fetchDeck = async () => {
      try {
        const response = await fetch('/api/decks/rider-waite')
        if (response.ok) {
          const data = await response.json()
          setDeck(data)
        } else {
          console.error('Failed to fetch deck:', response.status)
        }
      } catch (error) {
        console.error('Error fetching deck:', error)
      }
    }

    fetchDeck()
  }, [])

  const handleCardSelect = (event) => {
    const cardSlug = event.target.value
    if (cardSlug) {
      const card = deck.cards.find(c => c.slug === cardSlug)
      // Generate random rotation between -10 and 10 degrees
      const rotation = Math.random() * 20 - 10
      setSelectedCard(card)
      setCardRotation(rotation)
      setTotalRotation(rotation)
      setIsReversed(false)
    } else {
      setSelectedCard(null)
      setCardRotation(0)
      setTotalRotation(0)
      setIsReversed(false)
    }
  }

  const [isReversed, setIsReversed] = useState(false)
  const [isExpanded, setIsExpanded] = useState(false)

  const handleReverseToggle = () => {
    // Add 180 degrees to continue rotation in the same direction
    setTotalRotation(prev => prev + 180)
    // Toggle the reversed state
    setIsReversed(prev => !prev)
  }

  const handleClearCard = () => {
    setSelectedCard(null)
    setCardRotation(0)
    setTotalRotation(0)
    setIsReversed(false)
    setIsExpanded(false)
  }

  const handleCardClick = () => {
    setIsExpanded(true)
  }

  const handleCloseExpanded = () => {
    setIsExpanded(false)
  }

  // Helper function to normalize suite names
  const normalizeSuiteName = (suite) => {
    if (!suite) return 'Major Arcana'
    if (suite === 'major-arcana') return 'Major Arcana'
    // Capitalize first letter
    return suite.charAt(0).toUpperCase() + suite.slice(1)
  }

  // Group cards by suite and order them with Major Arcana first
  const groupedCards = deck ? deck.cards.reduce((acc, card) => {
    const suite = normalizeSuiteName(card.suite)
    if (!acc[suite]) {
      acc[suite] = []
    }
    acc[suite].push(card)
    return acc
  }, {}) : {}

  // Order the suites with Major Arcana first
  const suiteOrder = ['Major Arcana', 'Cups', 'Pentacles', 'Swords', 'Wands']
  const orderedGroupedCards = suiteOrder.reduce((acc, suite) => {
    if (groupedCards[suite]) {
      acc[suite] = groupedCards[suite]
    }
    return acc
  }, {})

  return (
    <div className="page">
      <Navbar pageTitle="Sacred Spread" />
      <div className="page-content">
        <div className="card-selector-container">
          <select
            id="card-select"
            className="card-selector"
            onChange={handleCardSelect}
            value={selectedCard?.slug || ''}
          >
            <option value="">Select a card...</option>
            {Object.entries(orderedGroupedCards).map(([suite, cards]) => (
              <optgroup key={suite} label={suite}>
                {cards.map(card => (
                  <option key={card.slug} value={card.slug}>
                    {card.name}
                  </option>
                ))}
              </optgroup>
            ))}
          </select>
        </div>

        {selectedCard && (
          <div className="card-details-container">
            <div className="card-image-wrapper">
              <button
                className="card-action-button clear-button"
                onClick={handleClearCard}
                title="Clear card"
              >
                ✕
              </button>
              <button
                className="card-action-button reverse-button"
                onClick={handleReverseToggle}
                title={isReversed ? "Show upright" : "Show reversed"}
              >
                ⟲
              </button>
              <div className="card-image-display" onClick={handleCardClick}>
                <img
                  src={selectedCard.imageUrl}
                  alt={selectedCard.name}
                  style={{ transform: `rotate(${totalRotation}deg)` }}
                  className={isReversed ? 'reversed' : ''}
                />
              </div>
            </div>
            <div className="card-name">{selectedCard.name}</div>
            {isReversed && <div className="card-orientation">(Reversed)</div>}
            <div className="card-info">
              <div className="card-meaning">
                <div className="info-title">✦ {isReversed ? 'Reversed Meaning' : 'Meaning'} ✦</div>
                <div className="info-content">
                  {isReversed ? selectedCard.reversedMeaning : selectedCard.meaning}
                </div>
              </div>
              <div className="card-symbols">
                <div className="info-title">✧ Symbols ✧</div>
                <div className="symbols-container">
                  {selectedCard.symbols && selectedCard.symbols.length > 0 ? (
                    selectedCard.symbols.map((symbol, index) => (
                      <span key={index} className="symbol-tag">{symbol}</span>
                    ))
                  ) : (
                    <div className="info-content">No symbols available</div>
                  )}
                </div>
              </div>
            </div>
          </div>
        )}

        {isExpanded && selectedCard && (
          <div className="expanded-overlay" onClick={handleCloseExpanded}>
            <button className="expanded-close-button" onClick={handleCloseExpanded}>
              ✕
            </button>
            <div className="expanded-image-container">
              <img
                src={selectedCard.imageUrl}
                alt={selectedCard.name}
                style={{ transform: isReversed ? 'rotate(180deg)' : 'rotate(0deg)' }}
                className="expanded-image"
              />
            </div>
          </div>
        )}
      </div>
    </div>
  )
}

export default AssistedReading
