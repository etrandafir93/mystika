import { Link } from 'react-router-dom'
import './AssistedReading.css'

function AssistedReading() {
  return (
    <div className="page">
      <div className="page-content">
        <Link to="/" className="back-button">← Back to Home</Link>
        <h1 className="page-title">Sacred Spread</h1>
        <p className="page-subtitle">Honor your earthly deck</p>
      </div>
    </div>
  )
}

export default AssistedReading
