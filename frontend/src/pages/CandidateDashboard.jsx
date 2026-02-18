import React, { useEffect, useState } from "react";
import SectionHeader from "../components/SectionHeader.jsx";
import { fetchCandidate, getRecommendations, uploadResume, updateCandidate } from "../api/candidate.js";
import { applyToPosting, fetchCandidateApplicationsPaged } from "../api/applications.js";
import { fetchNotifications, markNotificationRead } from "../api/notifications.js";

const CandidateDashboard = () => {
  const candidateId = localStorage.getItem("skillmerge_userId");
  const [candidate, setCandidate] = useState(null);
  const [recommendations, setRecommendations] = useState([]);
  const [applications, setApplications] = useState([]);
  const [applicationsPage, setApplicationsPage] = useState(0);
  const [applicationsTotal, setApplicationsTotal] = useState(1);
  const [notifications, setNotifications] = useState([]);
  const [notificationsPage, setNotificationsPage] = useState(0);
  const [notificationsTotal, setNotificationsTotal] = useState(1);
  const [resumeFile, setResumeFile] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const loadData = async (appsPage = applicationsPage, notesPage = notificationsPage) => {
    try {
      setLoading(true);
      setError(null);
      const profile = await fetchCandidate(candidateId);
      setCandidate(profile);
      const matches = await getRecommendations({ candidateId: Number(candidateId) });
      setRecommendations(matches.slice(0, 6));
      const apps = await fetchCandidateApplicationsPaged(candidateId, { page: appsPage, size: 4 });
      setApplications(apps.items || []);
      setApplicationsTotal(apps.totalPages || 1);
      const notes = await fetchNotifications(candidateId, { page: notesPage, size: 4 });
      setNotifications(notes.items || []);
      setNotificationsTotal(notes.totalPages || 1);
    } catch (err) {
      setError(err.message || "Failed to load candidate data. Ensure backend is running.");
      console.error("Error loading candidate data:", err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    if (candidateId) {
      loadData();
    } else {
      setError("No user ID found. Please log in.");
      setLoading(false);
    }
  }, [candidateId]);

  const handleResumeUpload = async () => {
    if (!resumeFile) return;
    await uploadResume(candidateId, resumeFile);
    await loadData();
  };

  const handleApply = async (match) => {
    const payload = {
      candidateId: Number(candidateId),
      type: match.type,
      jobId: match.type === "JOB" ? match.id : null,
      projectId: match.type === "PROJECT" ? match.id : null
    };
    await applyToPosting(payload);
    await loadData();
  };

  const handleUpdateSkills = async () => {
    if (!candidate) return;
    await updateCandidate(candidateId, candidate);
    await loadData();
  };

  const handleApplicationsPage = async (nextPage) => {
    setApplicationsPage(nextPage);
    await loadData(nextPage, notificationsPage);
  };

  const handleNotificationsPage = async (nextPage) => {
    setNotificationsPage(nextPage);
    await loadData(applicationsPage, nextPage);
  };

  const handleMarkRead = async (notificationId) => {
    await markNotificationRead(notificationId);
    await loadData();
  };

  if (loading) {
    return (
      <div className="max-w-6xl mx-auto p-6">
        <p className="text-center text-gray-600">Loading your dashboard...</p>
      </div>
    );
  }

  if (error) {
    return (
      <div className="max-w-6xl mx-auto p-6">
        <div className="card p-6 border-l-4 border-l-red-500 bg-red-50">
          <h3 className="font-semibold text-red-700 mb-2">Error loading dashboard</h3>
          <p className="text-red-600 text-sm">{error}</p>
          <button
            onClick={() => loadData()}
            className="mt-4 px-4 py-2 bg-red-600 text-white rounded-lg hover:bg-red-700 text-sm"
          >
            Retry
          </button>
        </div>
      </div>
    );
  }

  if (!candidate) {
    return (
      <div className="max-w-6xl mx-auto p-6">
        <p className="text-center text-gray-600">No candidate data available.</p>
      </div>
    );
  }

  return (
    <div className="grid gap-8">
      <SectionHeader title={`Hello ${candidate.name}`} subtitle="Your tailored recommendations and profile." />
      <div className="grid md:grid-cols-3 gap-6">
        <div className="card p-6 md:col-span-2">
          <h3 className="text-lg font-semibold">Recommended roles</h3>
          <div className="grid gap-4 mt-4">
            {recommendations.map((match) => (
              <div key={`${match.type}-${match.id}`} className="border rounded-xl p-4 flex justify-between items-center">
                <div>
                  <p className="font-semibold">{match.title}</p>
                  <p className="text-xs text-slate/70">{match.type} match</p>
                </div>
                <div className="flex items-center gap-3">
                  <span className="text-sm font-semibold text-mint">{match.matchPercent}%</span>
                  <button
                    onClick={() => handleApply(match)}
                    className="px-3 py-1 rounded-lg bg-ink text-white text-xs"
                  >
                    Apply
                  </button>
                </div>
              </div>
            ))}
          </div>
        </div>
        <div className="card p-6">
          <h3 className="text-lg font-semibold">Profile</h3>
          <div className="mt-4 grid gap-4">
            <label className="text-xs uppercase text-slate/60">Skills</label>
            <input
              value={candidate.skills?.join(", ") || ""}
              onChange={(event) =>
                setCandidate({
                  ...candidate,
                  skills: event.target.value.split(",").map((skill) => skill.trim())
                })
              }
              className="rounded-lg border p-2"
            />
            <label className="text-xs uppercase text-slate/60">Experience (years)</label>
            <input
              type="number"
              value={candidate.experience}
              onChange={(event) => setCandidate({ ...candidate, experience: Number(event.target.value) })}
              className="rounded-lg border p-2"
            />
            <button onClick={handleUpdateSkills} className="bg-ink text-white rounded-lg py-2">
              Save profile
            </button>
            <div className="mt-2">
              <label className="text-xs uppercase text-slate/60">Upload resume</label>
              <input type="file" onChange={(event) => setResumeFile(event.target.files[0])} className="mt-2" />
              <button onClick={handleResumeUpload} className="mt-2 px-3 py-1 bg-accent text-white rounded-lg text-xs">
                Upload
              </button>
            </div>
            {candidate.resumeUrl && (
              <a className="text-sm text-accent" href={candidate.resumeUrl}>
                View resume
              </a>
            )}
          </div>
        </div>
      </div>
      <div className="grid lg:grid-cols-2 gap-6">
        <div className="card p-6">
          <h3 className="text-lg font-semibold">Applications</h3>
          <div className="grid gap-3 mt-4">
            {applications.map((app) => (
              <div key={app.id} className="flex justify-between border rounded-lg p-3">
                <div>
                  <p className="font-medium">{app.type}</p>
                  <p className="text-xs text-slate/60">Status: {app.status}</p>
                </div>
                <p className="text-xs text-slate/60">{app.appliedDate}</p>
              </div>
            ))}
          </div>
          <div className="flex justify-between mt-4 text-xs">
            <button
              disabled={applicationsPage <= 0}
              onClick={() => handleApplicationsPage(applicationsPage - 1)}
              className="px-3 py-1 border rounded-lg"
            >
              Prev
            </button>
            <span>
              Page {applicationsPage + 1} of {applicationsTotal}
            </span>
            <button
              disabled={applicationsPage + 1 >= applicationsTotal}
              onClick={() => handleApplicationsPage(applicationsPage + 1)}
              className="px-3 py-1 border rounded-lg"
            >
              Next
            </button>
          </div>
        </div>
        <div className="card p-6">
          <h3 className="text-lg font-semibold">Notifications</h3>
          <div className="grid gap-3 mt-4">
            {notifications.map((note) => (
              <div key={note.id} className="flex justify-between border rounded-lg p-3">
                <div>
                  <p className="text-sm">{note.message}</p>
                  <p className="text-xs text-slate/60">{new Date(note.createdAt).toLocaleString()}</p>
                </div>
                {!note.read && (
                  <button onClick={() => handleMarkRead(note.id)} className="text-xs text-accent">
                    Mark read
                  </button>
                )}
              </div>
            ))}
          </div>
          <div className="flex justify-between mt-4 text-xs">
            <button
              disabled={notificationsPage <= 0}
              onClick={() => handleNotificationsPage(notificationsPage - 1)}
              className="px-3 py-1 border rounded-lg"
            >
              Prev
            </button>
            <span>
              Page {notificationsPage + 1} of {notificationsTotal}
            </span>
            <button
              disabled={notificationsPage + 1 >= notificationsTotal}
              onClick={() => handleNotificationsPage(notificationsPage + 1)}
              className="px-3 py-1 border rounded-lg"
            >
              Next
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default CandidateDashboard;
