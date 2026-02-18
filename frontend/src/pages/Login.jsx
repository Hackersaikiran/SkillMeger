import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import SectionHeader from "../components/SectionHeader.jsx";
import { login } from "../api/auth.js";

const Login = () => {
  const navigate = useNavigate();
  const [form, setForm] = useState({ email: "", password: "", role: "CANDIDATE" });
  const [errors, setErrors] = useState({});
  const [loading, setLoading] = useState(false);

  const validateForm = () => {
    const newErrors = {};
    if (!form.email) newErrors.email = "Email is required";
    else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(form.email)) newErrors.email = "Invalid email format";
    if (!form.password) newErrors.password = "Password is required";
    else if (form.password.length < 6) newErrors.password = "Password must be at least 6 characters";
    return newErrors;
  };

  const handleChange = (event) => {
    const { name, value } = event.target;
    setForm({ ...form, [name]: value });
    if (errors[name]) {
      setErrors({ ...errors, [name]: "" });
    }
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    const newErrors = validateForm();
    if (Object.keys(newErrors).length > 0) {
      setErrors(newErrors);
      return;
    }
    setLoading(true);
    try {
      const data = await login(form);
      localStorage.setItem("skillmerge_token", data.token);
      localStorage.setItem("skillmerge_role", data.role);
      localStorage.setItem("skillmerge_userId", data.userId);
      if (data.role === "ADMIN") {
        navigate("/admin");
      } else if (data.role === "RECRUITER") {
        navigate("/recruiter");
      } else {
        navigate("/candidate");
      }
    } catch (err) {
      setErrors({ submit: err.response?.data?.message || "Login failed. Check your credentials." });
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="max-w-lg mx-auto">
      <SectionHeader
        title="Welcome back"
        subtitle="Sign in to access tailored recommendations and analytics."
      />
      <div className="card p-8">
        <form onSubmit={handleSubmit} className="grid gap-4">
          <div>
            <label className="text-sm font-medium text-slate-700 block mb-1">Role</label>
            <select name="role" value={form.role} onChange={handleChange} className="">
              <option value="CANDIDATE">Candidate</option>
              <option value="RECRUITER">Recruiter</option>
              <option value="ADMIN">Admin</option>
            </select>
          </div>
          <div>
            <label className="text-sm font-medium text-slate-700 block mb-1">Email</label>
            <input 
              name="email" 
              type="email"
              placeholder="you@example.com"
              value={form.email} 
              onChange={handleChange} 
            />
            {errors.email && <span className="error-text">{errors.email}</span>}
          </div>
          <div>
            <label className="text-sm font-medium text-slate-700 block mb-1">Password</label>
            <input
              name="password"
              type="password"
              placeholder="••••••••"
              value={form.password}
              onChange={handleChange}
            />
            {errors.password && <span className="error-text">{errors.password}</span>}
          </div>
          {errors.submit && <div className="bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded-lg text-sm">{errors.submit}</div>}
          <button 
            disabled={loading}
            className="btn-primary mt-2"
          >
            {loading ? "Signing in..." : "Sign in"}
          </button>
          <p className="text-center text-sm text-slate-600">Don't have an account? <a href="/register" className="text-accent hover:underline font-medium">Register here</a></p>
        </form>
      </div>
    </div>
  );
};

export default Login;
