import React, { useContext, useEffect, useState } from "react";
import { tenants, institutions } from "../data";
import { Link, useParams } from "react-router-dom";
import { FaAngleRight } from "react-icons/fa";
import Navbar from "../Navbar";
import { Context } from "../Context";
import Loading from "./Loading";

const Institution = () => {
  //obtain id which is indicated in the url
  const { institutionName } = useParams();
  const { getInstitutionTenants } = useContext(Context);
  const [institutionState, setInstitutionState] = useState();
  console.log(institutionName);
  useEffect(() => {
    getInstitutionTenants(institutionName)
      .then((response) => {
        //update state of institution tenants
        setInstitutionState(response.data);
      })
      .catch(() => {
        console.log("Failed to get tenants in the institution");
      });
  }, []);

  // array of one object which is the selected institution
  const selectedInstitution = institutions.filter((institution) => {
    const { name } = institution;
    return institutionName === name;
  });

  return (
    <div>
      {institutionState ? (
        <div>
          <Navbar />
          {selectedInstitution.map((institution, index) => {
            const { id, name, imageUrl } = institution;
            return (
              <section key={index} className="institution-header">
                <img
                  src={imageUrl}
                  className="institution-logo"
                  alt="logo"
                ></img>
              </section>
            );
          })}
          {institutionState.map((tenant, index) => {
            const { store_name, acc_id } = tenant;
            return (
              <>
                <Link key={acc_id} to={`/tenant/${acc_id}`}>
                  <section className="institution-tenant-resolved">
                    <div>{store_name}</div>
                    <div className="institution-tenantbtn">
                      <FaAngleRight />
                    </div>
                  </section>
                </Link>
              </>
            );
          })}{" "}
        </div>
      ) : (
        <Loading />
      )}
    </div>
  );
};

export default Institution;
