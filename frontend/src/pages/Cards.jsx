import { useState } from 'react'
import { Link } from 'react-router-dom'
import { tarotCards } from '../data/tarotCards'
import './Cards.css'

function Cards() {
  const [selectedCard, setSelectedCard] = useState(tarotCards[0])

  return (
    <div className="cards-page">
      <div className="cards-header">
        <Link to="/" className="back-button">← Back to Home</Link>
        <h1 className="cards-page-title">The Cards</h1>
      </div>

      <div className="cards-container">
        {/* Left Sidebar - Card List */}
        <div className="cards-sidebar">
          <div className="sidebar-section">
            <h3 className="section-title">Major Arcana</h3>
            <ul className="cards-list">
              {tarotCards.filter(card => card.category === "Major Arcana").map(card => (
                <li
                  key={card.id}
                  className={`card-list-item ${selectedCard.id === card.id ? 'active' : ''}`}
                  onClick={() => setSelectedCard(card)}
                >
                  {card.name}
                </li>
              ))}
            </ul>
          </div>

          <div className="sidebar-section">
            <h3 className="section-title">Cups</h3>
            <ul className="cards-list">
              {tarotCards.filter(card => card.category === "Cups").map(card => (
                <li
                  key={card.id}
                  className={`card-list-item ${selectedCard.id === card.id ? 'active' : ''}`}
                  onClick={() => setSelectedCard(card)}
                >
                  {card.name}
                </li>
              ))}
            </ul>
          </div>

          <div className="sidebar-section">
            <h3 className="section-title">Pentacles</h3>
            <ul className="cards-list">
              {tarotCards.filter(card => card.category === "Pentacles").map(card => (
                <li
                  key={card.id}
                  className={`card-list-item ${selectedCard.id === card.id ? 'active' : ''}`}
                  onClick={() => setSelectedCard(card)}
                >
                  {card.name}
                </li>
              ))}
            </ul>
          </div>

          <div className="sidebar-section">
            <h3 className="section-title">Swords</h3>
            <ul className="cards-list">
              {tarotCards.filter(card => card.category === "Swords").map(card => (
                <li
                  key={card.id}
                  className={`card-list-item ${selectedCard.id === card.id ? 'active' : ''}`}
                  onClick={() => setSelectedCard(card)}
                >
                  {card.name}
                </li>
              ))}
            </ul>
          </div>

          <div className="sidebar-section">
            <h3 className="section-title">Wands</h3>
            <ul className="cards-list">
              {tarotCards.filter(card => card.category === "Wands").map(card => (
                <li
                  key={card.id}
                  className={`card-list-item ${selectedCard.id === card.id ? 'active' : ''}`}
                  onClick={() => setSelectedCard(card)}
                >
                  {card.name}
                </li>
              ))}
            </ul>
          </div>
        </div>

        {/* Main Content - Card Details */}
        <div className="card-details">
          <h2 className="card-name">{selectedCard.name}</h2>
          <p className="card-category">{selectedCard.category}</p>

          <div className="card-image-placeholder">
            <div className="placeholder-icon">🔮</div>
            <p className="placeholder-text">Image coming soon</p>
          </div>

          <div className="card-description">
            <h3 className="description-title">Meaning</h3>
            <p className="description-text">{selectedCard.description}</p>
          </div>
        </div>
      </div>
    </div>
  )
}

export default Cards
