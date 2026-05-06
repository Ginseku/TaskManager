import { Outlet } from "react-router-dom";
import Navbar from "../components/Navbar";
import "./Layout.css";

export default function AdminLayout() {
  return (
    <div>
      <Navbar />
      <main className="main-offset">
        <Outlet />
      </main>
    </div>
  );
}