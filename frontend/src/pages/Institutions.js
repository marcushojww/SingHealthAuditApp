import React from "react";
import { institutions } from "../data";
import { Link } from "react-router-dom";
import { FaAngleRight } from "react-icons/fa";
import Navbar from "../Navbar";
import InstitutionsCards from "../components/InstitutionsCards";

function Institutions() {
  return (
    <div>
      <Navbar />

      {/* <div className="institutions-head"> */}
      <InstitutionsCards />
      {/* {institutions.map((institution, index) => {
          const { id, name, tenantNames, imageUrl } = institution;
          return (
            <Link key={index} to={`/institution/${name}`}>
              <article key={id} className="institutions-institution">
                <img src={imageUrl} className="hospital-logo"></img>
                <header className="institutions-btn">
                  <span className="institutions-btnicon">
                    <FaAngleRight />
                  </span>
                </header>
              </article>
            </Link>
          );
        })} */}
    </div>
  );
}

export default Institutions;
