import React, { useEffect, useContext } from "react";
import TenantHomeCards from "../components/TenantHomeCards";
import Navbar from "../Navbar";
import CssBaseline from "@material-ui/core/CssBaseline";
import { Context } from "../Context";
import Loading from "./Loading";
import TenantHomeTabs from "../components/TenantHomeTabs";

function TenantHome() {
  const {
    getTenantAudits,
    tenantState,
    setTenantState,
    getUserInfoNoParams,
    getReport,
  } = useContext(Context);

  useEffect(() => {
    async function getResponse() {
      try {
        const tenantId = await getUserInfoNoParams().then((response) => {
          console.log(response);
          return response.data.acc_id;
        });
        console.log(tenantId);
        const reportIdArray = await getTenantAudits(tenantId).then(
          (response) => {
            if (response.data.LATEST === -1) {
              return [...response.data.CLOSED.past_audits];
            }
            return [response.data.LATEST, ...response.data.CLOSED.past_audits];
          }
        );
        console.log(reportIdArray);
        //initialize array to store all objects of report info
        let reportInfoArray = [];

        for (let i = 0; i < reportIdArray.length; i++) {
          let reportInfo = await getReport(reportIdArray[i]).then(
            (response) => {
              return response.data;
            }
          );
          reportInfoArray.push(reportInfo);
        }
        if (reportInfoArray.length === reportIdArray.length) {
          console.log(reportInfoArray);
          setTenantState(reportInfoArray);
        }
      } catch (err) {
        console.log(err);
      }
    }
    getResponse();
  }, []);
  return (
    <div>
      {tenantState ? (
        <>
          <CssBaseline />
          <Navbar />
          <TenantHomeTabs />
          <TenantHomeCards />
        </>
      ) : (
        <Loading />
      )}
    </div>
  );
}

export default TenantHome;
