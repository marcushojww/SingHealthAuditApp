import React, { useEffect, useContext } from "react";
import HomeAuditorCards from "../components/HomeAuditorCards";
import HomeAuditorTabs from "../components/HomeAuditorTabs";
import Navbar from "../Navbar";
import CssBaseline from "@material-ui/core/CssBaseline";
import { Context } from "../Context";
import Loading from "./Loading";

function HomeAuditor() {
  const { getAudits, auditsState, setAuditsState, getReport } = useContext(
    Context
  );

  useEffect(() => {
    const username = sessionStorage.getItem("authenticatedUser");

    async function getResponse() {
      try {
        const reportIdArray = await getAudits(username).then((response) => {
          console.log(response);
          return [
            ...response.data.CLOSED.completed_audits,
            ...response.data.OPEN.outstanding_audits,
          ];
        });
        //initialize array to store all objects of report info
        console.log("reportIDArray: " + reportIdArray);
        let reportInfoArray = [];

        for (let i = 0; i < reportIdArray.length; i++) {
          let reportInfo = await getReport(reportIdArray[i]).then(
            (response) => {
              console.log(response);
              return response.data;
            }
          );
          reportInfoArray.push(reportInfo);
        }
        console.log("reportInfoArray: " + reportInfoArray);

        //set state of audits to be an array of report info objects
        if (reportInfoArray.length === reportIdArray.length) {
          setAuditsState(reportInfoArray);
        }
      } catch (err) {
        console.log(err);
      }
    }
    getResponse();
  }, []);

  return (
    <div>
      {auditsState ? (
        <>
          <CssBaseline />
          <Navbar />
          <HomeAuditorTabs />
          <HomeAuditorCards />
        </>
      ) : (
        <Loading />
      )}
    </div>
  );
}

export default HomeAuditor;
