import React, { useState } from "react";
import SectionHeader from "../components/SectionHeader.jsx";
import { registerCandidate, registerRecruiter, registerAdmin } from "../api/auth.js";

const Register = () => {
  const [role, setRole] = useState("CANDIDATE");
  const [message, setMessage] = useState("");
  const [loading, setLoading] = useState(false);
  const [errors, setErrors] = useState({});
  const [form, setForm] = useState({
    name: "",
    email: "",
    password: "",
    company: "",
    skills: "",
    experience: 0
  });

  const validateForm = () => {
    const newErrors = {};
    if (!form.name) newErrors.name = "Name is required";
    if (!form.email) newErrors.email = "Email is required";
    else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(form.email)) newErrors.email = "Invalid email format";
    if (!form.password) newErrors.password = "Password is required";
    else if (form.password.length < 6) newErrors.password = "Password must be at least 6 characters";
    if (role === "RECRUITER" && !form.company) newErrors.company = "Company is required";
    if (role === "CANDIDATE" && !form.skills) newErrors.skills = "At least one skill is required";
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
      if (role === "CANDIDATE") {
        await registerCandidate({
          name: form.name,
          email: form.email,
          password: form.password,
          skills: form.skills ? form.skills.split(",").map((skill) => skill.trim()) : [],
          experience: Number(form.experience)
        });
      } else if (role === "RECRUITER") {
        await registerRecruiter({
          name: form.name,
          email: form.email,
          password: form.password,
          company: form.company
        });
      } else {
        await registerAdmin({
          name: form.name,
          email: form.email,
          password: form.password
        });
      }
      setMessage("Registration successful. Redirecting to login...");
      setTimeout(() => window.location.href = "/login", 1500);
    } catch (err) {
      setMessage(err.response?.data?.message || "Registration failed. Try again.");
    }
  };

  return (
    <div className="max-w-4xl mx-auto">
      <SectionHeader title="Create your account" subtitle="Choose your role and start building matches." />
      <div className="card p-8">
        <form onSubmit={handleSubmit} className="grid gap-4">
          <label className="text-sm">
            Role
            <select value={role} onChange={(event) => setRole(event.target.value)} className="mt-1 w-full rounded-lg border p-2">
              <option value="CANDIDATE">Candidate</option>
              <option value="RECRUITER">Recruiter</option>
              <option value="ADMIN">Admin</option>
            </select>
          </label>
          <label className="text-sm">
            Name
            <input name="name" value={form.name} onChange={handleChange} className="mt-1 w-full rounded-lg border p-2" />
          </label>
          <label className="text-sm">
            Email
            <input name="email" value={form.email} onChange={handleChange} className="mt-1 w-full rounded-lg border p-2" />
          </label>
          <label className="text-sm">
            Password
            <input name="password" type="password" value={form.password} onChange={handleChange} className="mt-1 w-full rounded-lg border p-2" />
          </label>
          {role === "RECRUITER" && (
            <label className="text-sm">
              Company
              <input name="company" value={form.company} onChange={handleChange} className="mt-1 w-full rounded-lg border p-2" />
            </label>
          )}
          {role === "CANDIDATE" && (
            <>
              <label className="text-sm">
                Skills (comma separated)
                <input name="skills" value={form.skills} onChange={handleChange} className="mt-1 w-full rounded-lg border p-2" />
              </label>
              <label className="text-sm">
                Experience (years)
                <input
                  name="experience"
                  type="number"
                  value={form.experience}
                  onChange={handleChange}
                  className="mt-1 w-full rounded-lg border p-2"
                />
              </label>
            </>
          )}
          {message && (
            <p className={`text-sm ${message.includes("successful") ? "text-emerald-600" : "text-red-600"}`}>
              {message}
            </p>
          )}
          <button className="bg-ink text-white rounded-lg py-2 hover:bg-slate">Create account</button>
        </form>
      </div>
    </div>
  );
};

export default Register;
