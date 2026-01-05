import { useState, useEffect } from 'react'
import Navbar from '../components/Navbar'
import './AssistedReading.css'

function AssistedReading() {
  const [deck, setDeck] = useState(null)
  const [cards, setCards] = useState([]) // Array to hold multiple cards
  const [isExpanded, setIsExpanded] = useState(false)
  const [expandedCardIndex, setExpandedCardIndex] = useState(null)
  const [isAddingSecondCard, setIsAddingSecondCard] = useState(false)
  const [selectedCategory, setSelectedCategory] = useState('love') // love, career, or spirituality

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
    console.log('Card selected:', cardSlug)
    console.log('Current state - cards.length:', cards.length, 'isAddingSecondCard:', isAddingSecondCard)

    if (cardSlug && deck) {
      const card = deck.cards.find(c => c.slug === cardSlug)
      console.log('Found card:', card ? card.name : 'not found')

      if (card) {
        // Generate random rotation between -10 and 10 degrees
        const rotation = Math.random() * 20 - 10
        const newCard = {
          ...card,
          rotation: rotation,
          totalRotation: rotation,
          isReversed: false
        }

        if (cards.length === 0) {
          console.log('Adding first card')
          setCards([newCard])
        } else if (cards.length === 1 && !isAddingSecondCard) {
          console.log('Replacing first card')
          setCards([newCard])
        } else if (cards.length === 1 && isAddingSecondCard) {
          console.log('Adding second card')
          setCards([...cards, newCard])
          setIsAddingSecondCard(false)
        }

        // Reset the select element
        event.target.value = ''
      }
    }
  }

  const handleReverseToggle = (index) => {
    const newCards = [...cards]
    newCards[index].totalRotation -= 180 // Rotate counterclockwise (to the left)
    newCards[index].isReversed = !newCards[index].isReversed
    setCards(newCards)
  }

  const handleClearCard = (index) => {
    const newCards = cards.filter((_, i) => i !== index)
    setCards(newCards)
    // Reset adding second card state if we're back to 1 or 0 cards
    if (newCards.length < 2) {
      setIsAddingSecondCard(false)
    }
  }

  const handleAddCard = () => {
    setIsAddingSecondCard(true)
  }

  const handleCardClick = (index) => {
    setExpandedCardIndex(index)
    setIsExpanded(true)
  }

  const handleCloseExpanded = () => {
    setIsExpanded(false)
    setExpandedCardIndex(null)
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
            disabled={cards.length >= 2}
          >
            <option value="">Select a card...</option>
            {Object.entries(orderedGroupedCards).map(([suite, cardsList]) => {
              const filteredCards = cardsList.filter(card => {
                // When adding second card, filter out the first card
                if (isAddingSecondCard && cards.length === 1) {
                  return !cards.some(c => c.slug === card.slug)
                }
                // Otherwise show all cards
                return true
              })

              return (
                <optgroup key={suite} label={suite}>
                  {filteredCards.map(card => (
                    <option key={card.slug} value={card.slug}>
                      {card.name}
                    </option>
                  ))}
                </optgroup>
              )
            })}
          </select>
        </div>

        {cards.length > 0 && (
          <>
            <div className={`cards-display ${cards.length === 2 ? 'two-cards' : 'one-card'}`}>
              {cards.map((card, index) => (
                <div key={index} className="card-column">
                  <div className="card-image-wrapper">
                    <button
                      className="card-action-button clear-button"
                      onClick={() => handleClearCard(index)}
                      title="Clear card"
                    >
                      ✕
                    </button>
                    <button
                      className="card-action-button reverse-button"
                      onClick={() => handleReverseToggle(index)}
                      title={card.isReversed ? "Show upright" : "Show reversed"}
                    >
                      ⟲
                    </button>
                    {/* Temporarily hidden - to be implemented later
                    {cards.length === 1 && (
                      <button
                        className={`card-action-button add-button ${isAddingSecondCard ? 'active' : ''}`}
                        onClick={handleAddCard}
                        title="Add another card"
                      >
                        +
                      </button>
                    )}
                    */}
                    <div className="card-image-display" onClick={() => handleCardClick(index)}>
                      <img
                        src={card.imageUrl}
                        alt={card.name}
                        style={{ transform: `rotate(${card.totalRotation}deg)` }}
                        className={card.isReversed ? 'reversed' : ''}
                      />
                    </div>
                  </div>
                  <div className="card-name">{card.name}</div>
                  {card.isReversed && <div className="card-orientation">(Reversed)</div>}

                  {cards.length === 1 && (
                    <div className="card-info">
                      <div className="category-buttons">
                        <button
                          className={`category-btn ${selectedCategory === 'love' ? 'active' : ''}`}
                          onClick={() => setSelectedCategory('love')}
                        >
                          Love
                        </button>
                        <button
                          className={`category-btn ${selectedCategory === 'spirituality' ? 'active' : ''}`}
                          onClick={() => setSelectedCategory('spirituality')}
                        >
                          Spirituality
                        </button>
                        <button
                          className={`category-btn ${selectedCategory === 'career' ? 'active' : ''}`}
                          onClick={() => setSelectedCategory('career')}
                        >
                          Career
                        </button>
                      </div>
                      <div className="card-meaning">
                        <div className="info-title">✦ {card.isReversed ? 'Reversed Meaning' : 'Meaning'} ✦</div>
                        <div className="info-content">
                          {card.detailedMeaning && card.detailedMeaning[selectedCategory]
                            ? (card.isReversed
                                ? card.detailedMeaning[selectedCategory].reversed
                                : card.detailedMeaning[selectedCategory].upright)
                            : (card.isReversed ? card.reversedMeaning : card.meaning)}
                        </div>
                      </div>
                      <div className="card-symbols">
                        <div className="info-title">✧ Symbols ✧</div>
                        <div className="symbols-container">
                          {card.symbols && card.symbols.length > 0 ? (
                            card.symbols.map((symbol, idx) => (
                              <span key={idx} className="symbol-tag">{symbol}</span>
                            ))
                          ) : (
                            <div className="info-content">No symbols available</div>
                          )}
                        </div>
                      </div>
                    </div>
                  )}
                </div>
              ))}
            </div>

            {cards.length === 2 && (
              <div className="card-comparison-reading">
                <p className="reading-text">
                  Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.
                </p>
              </div>
            )}
          </>
        )}

        {isExpanded && expandedCardIndex !== null && cards[expandedCardIndex] && (
          <div className="expanded-overlay" onClick={handleCloseExpanded}>
            <button className="expanded-close-button" onClick={handleCloseExpanded}>
              ✕
            </button>
            <div className="expanded-image-container">
              <img
                src={cards[expandedCardIndex].imageUrl}
                alt={cards[expandedCardIndex].name}
                style={{ transform: cards[expandedCardIndex].isReversed ? 'rotate(180deg)' : 'rotate(0deg)' }}
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
