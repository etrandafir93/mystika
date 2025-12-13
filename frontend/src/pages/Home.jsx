import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import './Home.css'

function Home() {
  const [stars, setStars] = useState([])

  useEffect(() => {
    const newStars = Array.from({ length: 100 }, (_, i) => ({
      id: i,
      left: Math.random() * 100,
      top: Math.random() * 100,
      size: Math.random() * 3 + 1,
      delay: Math.random() * 3
    }))
    setStars(newStars)
  }, [])

  return (
    <div className="app">
      <div className="stars">
        {stars.map(star => (
          <div
            key={star.id}
            className="star"
            style={{
              left: `${star.left}%`,
              top: `${star.top}%`,
              width: `${star.size}px`,
              height: `${star.size}px`,
              animationDelay: `${star.delay}s`
            }}
          />
        ))}
      </div>

      <div className="content">
        <div className="logo">
          <div className="mystical-circle">
            <div className="inner-circle"></div>
            <div className="moon-symbol">☽</div>
          </div>
        </div>

        <h1 className="title">Mystika</h1>
        <p className="subtitle">Unveil the Mysteries of Your Destiny</p>

        <div className="navigation-buttons">
          <Link to="/digital-reading" className="nav-button">
            <div className="button-icon">🔮</div>
            <h3>Celestial Reading</h3>
            <p>Cards from the cosmos</p>
          </Link>

          <Link to="/physical-reading" className="nav-button">
            <div className="button-icon">📖</div>
            <h3>Sacred Spread</h3>
            <p>Honor your earthly deck</p>
          </Link>
        </div>
      </div>
    </div>
  )
}

export default Home
