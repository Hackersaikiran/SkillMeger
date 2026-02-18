import React from "react";

const StatCard = ({ label, value, accent }) => {
  return (
    <div className={`card p-5 border-l-4 ${accent}`}>
      <p className="text-xs uppercase tracking-wide text-slate/60">{label}</p>
      <p className="text-2xl font-semibold mt-2">{value}</p>
    </div>
  );
};

export default StatCard;
