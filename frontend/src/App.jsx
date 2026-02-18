import React from "react";
import { Navigate, Route, Routes } from "react-router-dom";
import Layout from "./components/Layout.jsx";
import Landing from "./pages/Landing.jsx";
import Login from "./pages/Login.jsx";
import Register from "./pages/Register.jsx";
import CandidateDashboard from "./pages/CandidateDashboard.jsx";
import RecruiterDashboard from "./pages/RecruiterDashboard.jsx";
import AdminDashboard from "./pages/AdminDashboard.jsx";
import JobSearch from "./pages/JobSearch.jsx";

const RequireRole = ({ allowedRoles, children }) => {
  const role = localStorage.getItem("skillmerge_role");
  if (!role) {
    return <Navigate to="/login" replace />;
  }
  if (!allowedRoles.includes(role)) {
    return <Navigate to="/search" replace />;
  }
  return children;
};

const App = () => {
  return (
    <Layout>
      <Routes>
        <Route path="/" element={<Landing />} />
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route
          path="/candidate"
          element={(
            <RequireRole allowedRoles={["CANDIDATE"]}>
              <CandidateDashboard />
            </RequireRole>
          )}
        />
        <Route
          path="/recruiter"
          element={(
            <RequireRole allowedRoles={["RECRUITER"]}>
              <RecruiterDashboard />
            </RequireRole>
          )}
        />
        <Route
          path="/admin"
          element={(
            <RequireRole allowedRoles={["ADMIN"]}>
              <AdminDashboard />
            </RequireRole>
          )}
        />
        <Route path="/search" element={<JobSearch />} />
      </Routes>
    </Layout>
  );
};

export default App;
