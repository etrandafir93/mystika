import { Link } from 'react-router-dom'
import './DigitalDrawing.css'

function DigitalDrawing() {
  return (
    <div className="page">
      <div className="page-content">
        <Link to="/" className="back-button">← Back to Home</Link>
        <h1 className="page-title">Celestial Reading</h1>
        <p className="page-subtitle">Cards from the cosmos</p>
      </div>
    </div>
  )
}

export default DigitalDrawing
