import React from 'react'
import ReactDOM from 'react-dom/client'
import App from './App.jsx'
import 'bootstrap/dist/css/bootstrap.min.css';
import { BrowserRouter } from 'react-router-dom';
import { DostawcaAutoryzacji } from './context/KontekstAutoryzacji';

ReactDOM.createRoot(document.getElementById('root')).render(
  <React.StrictMode>
    <BrowserRouter>
      <DostawcaAutoryzacji>
        <App />
      </DostawcaAutoryzacji>
    </BrowserRouter>
  </React.StrictMode>,
)
