import { Link } from 'react-router-dom'
import './Navbar.css'

function Navbar({ pageTitle }) {
  return (
    <nav className="navbar">
      <Link to="/" className="nav-home">
        <span className="nav-home-icon">Mystika</span>
      </Link>
    </nav>
  )
}

export default Navbar
