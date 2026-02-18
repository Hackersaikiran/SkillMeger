import React from "react";
import { Link, useLocation } from "react-router-dom";

const Layout = ({ children }) => {
  const location = useLocation();
  const role = localStorage.getItem("skillmerge_role");

  const navItems = [
    { label: "Search", to: "/search", roles: ["CANDIDATE", "RECRUITER", "ADMIN"] },
    { label: "Candidate", to: "/candidate", roles: ["CANDIDATE"] },
    { label: "Recruiter", to: "/recruiter", roles: ["RECRUITER"] },
    { label: "Admin", to: "/admin", roles: ["ADMIN"] }
  ];

  const visibleNav = role ? navItems.filter((item) => item.roles.includes(role)) : [];

  const handleLogout = () => {
    localStorage.removeItem("skillmerge_token");
    localStorage.removeItem("skillmerge_role");
    localStorage.removeItem("skillmerge_userId");
    window.location.href = "/login";
  };

  return (
    <div className="min-h-screen">
      <header className="site-header px-6 py-5 flex items-center justify-between">
        <Link to={role ? "/search" : "/"} className="brand">
          SkillMerge
        </Link>
        <nav className="flex flex-wrap items-center gap-3 text-sm">
          {visibleNav.map((item) => {
            const isActive = location.pathname === item.to;
            return (
              <Link
                key={item.to}
                to={item.to}
                className={`nav-pill ${isActive ? "nav-pill--active" : ""}`}
              >
                {item.label}
              </Link>
            );
          })}
          {!role && (
            <>
              <Link to="/login" className="nav-pill">
                Login
              </Link>
              <Link to="/register" className="nav-pill nav-pill--primary">
                Register
              </Link>
            </>
          )}
          {role && (
            <button type="button" onClick={handleLogout} className="nav-pill nav-pill--ghost">
              Logout
            </button>
          )}
        </nav>
      </header>
      <main className="px-6 pb-16">{children}</main>
    </div>
  );
};

export default Layout;
