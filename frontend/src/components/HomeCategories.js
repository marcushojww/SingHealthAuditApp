import React from "react";
import { categories } from "../data";

function HomeCategories({ filterAudits }) {
  return (
    <div className="home-categories">
      {categories.map((category, index) => {
        return (
          <button
            className="btn-category"
            key={index}
            onClick={() => {
              filterAudits(category);
            }}
          >
            {category}
          </button>
        );
      })}
    </div>
  );
}

export default HomeCategories;
