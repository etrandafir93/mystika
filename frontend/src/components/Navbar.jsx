import { Link } from 'react-router-dom'
import './Navbar.css'

function Navbar({ pageTitle }) {
  return (
    <nav className="navbar">
      <Link to="/" className="nav-home">
        <span className="nav-home-icon">☽</span>
      </Link>
      <h2 className="nav-title">{pageTitle}</h2>
    </nav>
  )
}

export default Navbar
