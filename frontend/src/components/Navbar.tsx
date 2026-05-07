import { Link } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import { routes } from "../router/routes";
import "./Navbar.css";
import { useTheme } from "../context/ThemeContext";
import { useNavigate, useLocation } from "react-router-dom";
import logo from "../assets/Logo.png";
import { useState } from "react";

export default function Navbar() {
  const { user, logout } = useAuth();
  const { theme, toggleTheme } = useTheme();
  const navigate = useNavigate();
  const location = useLocation();
  const isAdminPage = location.pathname.startsWith("/admin");
  const isAboutPage = location.pathname === routes.about();
  const [menuOpen, setMenuOpen] = useState(false);

  const handleLogout = () => {
    logout();
    //small delay to ensure state clears so user gets back to login screen
    setTimeout(() => {
      navigate("/", { replace: true });
    }, 0);
  };

  return (
    <header className="navbar">
      {/* Left */}
      <div className="navbar-left">
        <div className="navbar-title">
          <Link
            to={routes.dashboard()}
            className="logo-link"
            style={{ textDecoration: 'none' }}
          >
            <img src={logo} alt="logo" className="navbar-logo" />
            <span className="navbar-brand">Task Manager</span>
          </Link>
          

          {user && <span className="navbar-user">{user.name}</span>}
        </div>
      </div>

      {/* Center */}
      <div className="navbar-center">
        <button
          onClick={toggleTheme}
          style={{
            marginRight: 12,
            background: "transparent",
            border: "1px solid var(--border)",
            borderRadius: 6,
            padding: "4px 8px",
            cursor: "pointer",
            color: "var(--text)",
          }}
        >
          {theme === "dark" ? "🌙 Dark" : "☀️ Light"}
        </button>
      </div>

      {/* Right */}
      <div className="navbar-right">
        <button
          className="navbar-hamburger"
          onClick={() => setMenuOpen(!menuOpen)}
        >
          <span className="material-symbols-outlined">
            {menuOpen ? "close" : "menu"}
          </span>
        </button>
        <div className={`navbar-menu ${menuOpen ? "open" : ""}`}>
          {isAboutPage ? (
            <Link
              to={routes.dashboard()}
              className="navbar-button"
              style={{ marginRight: 12 }}
            >
              Home
            </Link>
          ) : (
            <Link
              to={routes.about()}
              className="navbar-button"
              style={{ marginRight: 12 }}
            >
              About
            </Link>
          )}
          {location.pathname === "/admin" && (
            <Link
              to={routes.dashboard()}
              className="navbar-button"
              style={{ marginRight: 12 }}
            >
              Back to App
            </Link>
          )}
          {user?.role === "ADMIN" && !isAdminPage && (
            <Link
              to={routes.admin()}
              className="navbar-button"
              style={{ marginRight: 12 }}
            >
              Admin Panel
            </Link>
          )}
          {user ? (
            <button onClick={handleLogout} className="navbar-button">
              Logout
            </button>
          ) : (
            <>
              <Link to={routes.login()} className="navbar-link">
                Login
              </Link>
              <Link to={routes.register()} className="navbar-link">
                Register
              </Link>
            </>
          )}
        </div>
      </div>
    </header>
  );
}
