import { Routes, Route } from 'react-router-dom'
import Home from './pages/Home'
import DigitalDrawing from './pages/DigitalDrawing'
import AssistedReading from './pages/AssistedReading'
import Cards from './pages/Cards'

function App() {
  return (
    <Routes>
      <Route path="/" element={<Home />} />
      <Route path="/digital-reading" element={<DigitalDrawing />} />
      <Route path="/physical-reading" element={<AssistedReading />} />
      <Route path="/cards" element={<Cards />} />
    </Routes>
  )
}

export default App
