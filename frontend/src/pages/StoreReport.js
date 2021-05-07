import React, { useState, useEffect, useContext } from "react";
import Navbar from "../Navbar";
import { Context } from "../Context";
import { useParams } from "react-router-dom";
import List from "@material-ui/core/List";
import { makeStyles } from "@material-ui/core/styles";
import ReportQuestion from "../components/ReportQuestion";
import Box from "@material-ui/core/Box";
import { Typography } from "@material-ui/core";

const useStyles = makeStyles((theme) => ({
  header: {
    backgroundColor: theme.palette.background.paper,
    padding: theme.spacing(2, 0, 2, 0),
  },
  root: {
    display: "flex",
    flexDirection: "column",
    alignItems: "center",
    padding: theme.spacing(4, 0, 10, 0),
  },
  list: {
    width: "100%",
    maxWidth: 800,
    backgroundColor: theme.palette.background.paper,
    display: "flex",
    flexDirection: "column",

    // alignItems: "center",
  },
}));

function StoreReport() {
  const classes = useStyles();
  const { reportId } = useParams();
  const { getReport, getQuestionInfo, getOriginalReport } = useContext(Context);
  const [questions, setQuestions] = useState();

  useEffect(() => {
    async function storeReportAsync() {
      try {
        const reportQuestions = await getOriginalReport(reportId).then(
          (response) => {
            console.log(response);
            return response.data;
          }
        );
        setQuestions(reportQuestions);

        const reportInfo = await getReport(reportId).then((response) => {
          console.log(response.data);
          return response.data;
        });
      } catch (err) {
        console.log(err);
      }
    }
    storeReportAsync();
  }, []);
  return (
    <>
      {questions && (
        <div>
          <Navbar />
          <Box className={classes.header} textAlign="center" boxShadow={1}>
            <Typography variant="h5">Full Report Overview</Typography>
          </Box>
          <div className={classes.root}>
            <List dense className={classes.list}>
              {questions.map((question, index) => {
                const {
                  qn_id,
                  Requirement,
                  images,
                  remarks,
                  status,
                  severity,
                } = question;
                let severityVar = severity / 1000000;
                severityVar = Math.floor(severityVar);
                let timeframe = "";
                switch (severityVar) {
                  case 1:
                    timeframe = "3 weeks";
                    break;
                  case 2:
                    timeframe = "2 weeks";
                    break;
                  case 3:
                    timeframe = "1 week";
                    break;
                  case 4:
                    timeframe = "4 days";
                    break;
                  case 5:
                    timeframe = "1 day";
                    break;
                }

                return (
                  <ReportQuestion
                    key={qn_id}
                    qn_id={qn_id}
                    requirement={Requirement}
                    images={images[0]}
                    remarks={remarks}
                    status={status}
                    severity={timeframe}
                  />
                );
              })}
            </List>
          </div>
        </div>
      )}
    </>
  );
}

export default StoreReport;
