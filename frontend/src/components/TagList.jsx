import React from "react";

const TagList = ({ items = [] }) => {
  return (
    <div className="flex flex-wrap gap-2">
      {items.map((item) => (
        <span key={item} className="px-3 py-1 text-xs rounded-full bg-amber-100 text-amber-800">
          {item}
        </span>
      ))}
    </div>
  );
};

export default TagList;
