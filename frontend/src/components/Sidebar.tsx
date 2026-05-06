import { NavLink } from "react-router-dom";
import { routes } from "../router/routes";
import "./Sidebar.css";
import { useEffect } from "react";
import { useAuth } from "../context/AuthContext";

type SidebarProps = {
  open: boolean;
  setOpen: React.Dispatch<React.SetStateAction<boolean>>;
};

export default function Sidebar({ open, setOpen }: SidebarProps) {
  const { user } = useAuth();

  //Auto close sibar on small screen
  useEffect(() => {
    const handleResize = () => {
      if (window.innerWidth < 768) {
        setOpen(false);
      }
    };

    handleResize();
    window.addEventListener("resize", handleResize);
    return () => window.removeEventListener("resize", handleResize);
  }, [setOpen]);

  return (
    <>
      <button className="sidebar-toggle" onClick={() => setOpen(!open)}>
        <span className="material-symbols-outlined">
          {open ? "chevron_left" : "menu"}
        </span>
      </button>
      <aside className={`sidebar ${open ? "open" : "closed"}`}>
        <h3 className="sidebar-title">Menu</h3>

        <nav className="sidebar-nav">
          {/* Dahboard */}
          <NavLink
            to={routes.dashboard()}
            className={({ isActive }) =>
              isActive ? "sidebar-link active" : "sidebar-link"
            }
          >
            Dashboard
          </NavLink>

          {/* Teams */}
          <NavLink
            to={routes.teams()}
            className={({ isActive }) =>
              isActive ? "sidebar-link active" : "sidebar-link"
            }
          >
            Teams
          </NavLink>

          {/* To be implemented */}
          <div className="sidebar-section">
            <p className="sidebar-section-title">Work</p>

            <div className="sidebar-link disabled">My Tasks</div>
            <div className="sidebar-link disabled">Projects</div>
          </div>

          {/* Admin */}
          {user?.role === "ADMIN" && (
            <div className="sidebar-section">
              <NavLink
                to={routes.admin()}
                className={({ isActive }) =>
                  isActive ? "sidebar-link active" : "sidebar-link"
                }
              >
                Admin Panel
              </NavLink>
            </div>
          )}
        </nav>
      </aside>
    </>
  );
}
