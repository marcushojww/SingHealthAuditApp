import React, { useRef } from "react";
import { FaAngleRight } from "react-icons/fa";
import { Link } from "react-router-dom";

function Audits({ homeAudits }) {
  //to display the audits on the home page
  //change color based on whether it is resolved or not
  const homeAuditGridRef = useRef(null);

  return (
    <div>
      {homeAudits.map((audit, index) => {
        const { tenantid, tenantName, timeRemaining, status, date } = audit;

        return (
          <Link key={index} to={`/tenant/${tenantid}`}>
            <div
              className={`${
                audit.status === "resolved"
                  ? "homeaudit-grid-resolved"
                  : "homeaudit-grid-unresolved"
              }`}
              ref={homeAuditGridRef}
            >
              <div className="tenantName-gridItem">{tenantName}</div>
              <div className="btn-homeToTenant">
                <FaAngleRight />
              </div>
            </div>
          </Link>
        );
      })}
    </div>
  );
}

export default Audits;
