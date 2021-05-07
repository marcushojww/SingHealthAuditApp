import React, { useState, useContext, useEffect, useCallback } from "react";
import { useParams, Link } from "react-router-dom";
import Question from "../components/Question";
import { Context } from "../Context";
import Loading from "./Loading";
import Navbar from "../Navbar";
import { makeStyles } from "@material-ui/core/styles";
import List from "@material-ui/core/List";
import Box from "@material-ui/core/Box";
import Button from "@material-ui/core/Button";
import Grid from "@material-ui/core/Grid";

import Typography from "@material-ui/core/Typography";

//styling for the fbchecklist page
const useStyles = makeStyles((theme) => ({
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
  button: {
    color: "#F15A22",
    fontWeight: "medium",
    width: "100%",
    // maxWidth: 800,
    backgroundColor: theme.palette.background.paper,
    height: 50,
  },
  header: {
    backgroundColor: theme.palette.background.paper,
    padding: theme.spacing(2, 2, 2, 2),
  },
  link: {
    width: "100%",
    maxWidth: 800,
    backgroundColor: theme.palette.background.paper,
  },
}));

function NonFbChecklist() {
  //use styles from function declared above
  const classes = useStyles();
  //get tenant id from url
  const { tenantId } = useParams();

  //Context
  const {
    getNonFbChecklistQuestions,
    createNonFbReportState,
    getUserInfo,
    nfbReportState,
    submitNonFbReport,
    tenantType,
    setTenantType,
  } = useContext(Context);

  //state to update nfb checklist questions
  const [nfbChecklistState, setNonFbChecklistState] = useState();
  const [tenantName, setTenantName] = useState();

  useEffect(() => {
    //function to retrieve questions
    async function getTenantName() {
      try {
        const tenant_name = await getUserInfo(tenantId).then((response) => {
          setTenantType(response.data.type_id);
          return response.data.store_name;
        });
        console.log(tenant_name);
        setTenantName(tenant_name);
      } catch (err) {
        console.log(err);
      }
    }
    getTenantName();
    getNonFbChecklistQuestions()
      .then((response) => {
        setNonFbChecklistState(response.data);
        createNonFbReportState(response.data);
      })
      .catch(() => {
        console.log("fb checklist retrieval failed");
      });
  }, []);

  const handleSubmit = (tenantid, report) => {
    submitNonFbReport(tenantid, report);
  };

  return (
    <>
      {nfbChecklistState && tenantName && tenantType ? (
        <>
          <Navbar />
          <Box className={classes.header} textAlign="center" boxShadow={1}>
            <Typography variant="h5">{tenantName} Non-F&B Checklist</Typography>
          </Box>

          <Grid container className={classes.root}>
            <List dense className={classes.list}>
              {nfbChecklistState.map((question, index) => {
                const { nfb_qn_id, requirement } = question;
                const labelId = `checkbox-list-secondary-label-${nfb_qn_id}`;
                return (
                  <>
                    <Question
                      key={nfb_qn_id}
                      fb_qn_id={nfb_qn_id}
                      requirement={requirement}
                      labelId={labelId}
                      type={tenantType}
                    />
                  </>
                );
              })}
            </List>
            <Link to={`/tenant/${tenantId}`} className={classes.link}>
              <Button
                className={classes.button}
                size="small"
                onClick={() => {
                  handleSubmit(tenantId, nfbReportState);
                }}
              >
                Submit
              </Button>
            </Link>
          </Grid>
        </>
      ) : (
        <Loading />
      )}
    </>
  );
}

export default NonFbChecklist;
