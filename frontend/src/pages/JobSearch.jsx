import React, { useEffect, useState } from "react";
import SectionHeader from "../components/SectionHeader.jsx";
import TagList from "../components/TagList.jsx";
import { searchJobs, searchProjects } from "../api/search.js";
import { applyToPosting } from "../api/applications.js";

const JobSearch = () => {
  const candidateId = localStorage.getItem("skillmerge_userId");
  const role = localStorage.getItem("skillmerge_role");
  const [skill, setSkill] = useState("");
  const [experience, setExperience] = useState("");
  const [jobs, setJobs] = useState([]);
  const [projects, setProjects] = useState([]);
  const [jobPage, setJobPage] = useState(0);
  const [projectPage, setProjectPage] = useState(0);
  const [jobTotal, setJobTotal] = useState(1);
  const [projectTotal, setProjectTotal] = useState(1);

  const buildParams = (pageValue) => {
    const normalizedSkill = skill.trim();
    const experienceValue = String(experience).trim();
    const parsedExperience = experienceValue === "" ? undefined : Number(experienceValue);
    const minExperience = parsedExperience != null && parsedExperience > 0 ? parsedExperience : undefined;
    return {
      skill: normalizedSkill || undefined,
      minExperience: Number.isNaN(minExperience) ? undefined : minExperience,
      page: pageValue,
      size: 4,
      sortBy: "title",
      direction: "asc"
    };
  };

  const handleSearch = async (nextJobPage = jobPage, nextProjectPage = projectPage) => {
    const jobResponse = await searchJobs(buildParams(nextJobPage));
    setJobs(jobResponse.items || []);
    setJobTotal(Math.max(jobResponse.totalPages || 1, 1));

    const projectResponse = await searchProjects(buildParams(nextProjectPage));
    setProjects(projectResponse.items || []);
    setProjectTotal(Math.max(projectResponse.totalPages || 1, 1));
  };

  useEffect(() => {
    handleSearch(0, 0);
  }, []);

  const handleJobPage = async (nextPage) => {
    setJobPage(nextPage);
    await handleSearch(nextPage, projectPage);
  };

  const handleProjectPage = async (nextPage) => {
    setProjectPage(nextPage);
    await handleSearch(jobPage, nextPage);
  };

  const handleApply = async (type, id) => {
    if (role !== "CANDIDATE") return;
    await applyToPosting({
      candidateId: Number(candidateId),
      type,
      jobId: type === "JOB" ? id : null,
      projectId: type === "PROJECT" ? id : null
    });
  };

  return (
    <div className="grid gap-8">
      <SectionHeader title="Search jobs and projects" subtitle="Filter by skill and experience." />
      <div className="card p-6 flex flex-wrap gap-3 items-end">
        <label className="text-sm flex-1">
          Skill
          <input value={skill} onChange={(event) => setSkill(event.target.value)} className="mt-1 w-full rounded-lg border p-2" />
        </label>
        <label className="text-sm">
          Experience
          <input
            type="number"
            value={experience}
            onChange={(event) => setExperience(event.target.value)}
            className="mt-1 w-full rounded-lg border p-2"
          />
        </label>
        <button
          onClick={() => {
            setJobPage(0);
            setProjectPage(0);
            handleSearch(0, 0);
          }}
          className="px-4 py-2 bg-ink text-white rounded-lg"
        >
          Search
        </button>
      </div>
      <div className="grid lg:grid-cols-2 gap-6">
        <div className="card p-6">
          <h3 className="text-lg font-semibold">Jobs</h3>
          {role !== "CANDIDATE" && (
            <p className="text-xs text-slate/60 mt-2">Apply is available for candidates only.</p>
          )}
          <div className="mt-4 grid gap-4">
            {jobs.map((job) => (
              <div key={job.id} className="border rounded-xl p-4">
                <div className="flex justify-between">
                  <p className="font-semibold">{job.title}</p>
                  {role === "CANDIDATE" && (
                    <button onClick={() => handleApply("JOB", job.id)} className="text-xs text-accent">
                      Apply
                    </button>
                  )}
                </div>
                <p className="text-xs text-slate/70 mt-2">{job.description}</p>
                <TagList items={job.skillsRequired || []} />
              </div>
            ))}
          </div>
          <div className="flex justify-between mt-4 text-xs">
            <button
              disabled={jobPage <= 0}
              onClick={() => handleJobPage(jobPage - 1)}
              className="px-3 py-1 border rounded-lg"
            >
              Prev
            </button>
            <span>
              Page {jobPage + 1} of {jobTotal}
            </span>
            <button
              disabled={jobPage + 1 >= jobTotal}
              onClick={() => handleJobPage(jobPage + 1)}
              className="px-3 py-1 border rounded-lg"
            >
              Next
            </button>
          </div>
        </div>
        <div className="card p-6">
          <h3 className="text-lg font-semibold">Projects</h3>
          {role !== "CANDIDATE" && (
            <p className="text-xs text-slate/60 mt-2">Apply is available for candidates only.</p>
          )}
          <div className="mt-4 grid gap-4">
            {projects.map((project) => (
              <div key={project.id} className="border rounded-xl p-4">
                <div className="flex justify-between">
                  <p className="font-semibold">{project.title}</p>
                  {role === "CANDIDATE" && (
                    <button onClick={() => handleApply("PROJECT", project.id)} className="text-xs text-accent">
                      Apply
                    </button>
                  )}
                </div>
                <p className="text-xs text-slate/70 mt-2">{project.description}</p>
                <TagList items={project.skillsRequired || []} />
              </div>
            ))}
          </div>
          <div className="flex justify-between mt-4 text-xs">
            <button
              disabled={projectPage <= 0}
              onClick={() => handleProjectPage(projectPage - 1)}
              className="px-3 py-1 border rounded-lg"
            >
              Prev
            </button>
            <span>
              Page {projectPage + 1} of {projectTotal}
            </span>
            <button
              disabled={projectPage + 1 >= projectTotal}
              onClick={() => handleProjectPage(projectPage + 1)}
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

export default JobSearch;
