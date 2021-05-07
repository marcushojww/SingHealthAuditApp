import React, { useContext, useEffect, useState } from "react";
import { Link, useParams } from "react-router-dom";
import { Context } from "../Context";
import Navbar from "../Navbar";
import Loading from "./Loading";
import { makeStyles } from "@material-ui/core/styles";
import List from "@material-ui/core/List";
import ListItem from "@material-ui/core/ListItem";
import ListItemIcon from "@material-ui/core/ListItemIcon";
import ListItemText from "@material-ui/core/ListItemText";
import Collapse from "@material-ui/core/Collapse";
import ExpandLess from "@material-ui/icons/ExpandLess";
import ExpandMore from "@material-ui/icons/ExpandMore";
import Typography from "@material-ui/core/Typography";
import Box from "@material-ui/core/Box";
import QuestionAnswerIcon from "@material-ui/icons/QuestionAnswer";
import HistoryIcon from "@material-ui/icons/History";
import AssignmentTurnedInIcon from "@material-ui/icons/AssignmentTurnedIn";
import FastfoodIcon from "@material-ui/icons/Fastfood";
import StoreIcon from "@material-ui/icons/Store";
import LocalHospitalIcon from "@material-ui/icons/LocalHospital";
import Grid from "@material-ui/core/Grid";
import ReceiptIcon from "@material-ui/icons/Receipt";
import EmailIcon from "@material-ui/icons/Email";
import Button from "@material-ui/core/Button";
import ReportProblemIcon from "@material-ui/icons/ReportProblem";
import CheckCircleIcon from "@material-ui/icons/CheckCircle";

const useStyles = makeStyles((theme) => ({
  root: {
    display: "flex",
    flexDirection: "column",
    alignItems: "center",
    padding: theme.spacing(4, 2, 10, 2),
  },
  list: {
    width: "100%",
    maxWidth: 800,
    backgroundColor: theme.palette.background.paper,
    // margin: theme.spacing(4, 0, 10, 0),
  },
  nested: {
    // display: "flex",
    paddingLeft: theme.spacing(4),
  },
  header: {
    backgroundColor: theme.palette.background.paper,
    padding: theme.spacing(2, 0, 2, 0),
  },
  listItem: {
    padding: theme.spacing(2, 2, 2, 2),
  },
  previousAuditsButtons: {
    display: "flex",
    padding: theme.spacing(0, 2, 3, 2),
    justifyContent: "center",
    // justifyContent: "space-evenly",
  },
  button: {
    margin: theme.spacing(1, 2, 1, 2),
  },
  resolvedLabel: {
    color: "#F15A22",
    padding: theme.spacing(0, 2, 0, 2),
  },
}));

function Tenant() {
  //get tenantid from url
  const { tenantId } = useParams();
  // console.log(tenantId);
  //tenant state
  const [tenantState, setTenantState] = useState();
  const [tenantAudits, setTenantAudits] = useState();
  const [openChecklist, setOpenChecklist] = useState(false);
  const [openPrevAudits, setOpenPrevAudits] = useState(false);
  //Context: getUserInfo method
  const { getUserInfo, getTenantAudits, getReport, getAudits } = useContext(
    Context
  );

  const classes = useStyles();

  useEffect(() => {
    getUserInfo(tenantId)
      .then((response) => {
        // console.log(response);
        setTenantState(response.data);
      })
      .catch(() => {
        console.log("Failed to retrieve tenant info");
      });

    // console.log(tenantId);
    const username = sessionStorage.getItem("authenticatedUser");
    async function getResponse() {
      try {
        const reportIdArray = await getTenantAudits(tenantId).then(
          (response) => {
            console.log(response);

            if (response.data.LATEST === -1) {
              return [...response.data.CLOSED.past_audits];
            }

            return [...response.data.CLOSED.past_audits, response.data.LATEST];

            // return [response.data.LATEST, ...response.data.CLOSED];
          }
        );
        // console.log(reportIdArray);
        // console.log(reportIdArray);

        // const reportIdArray = await getAudits(username).then((response) => {
        //   console.log(response);
        //   return [
        //     ...response.data.CLOSED.completed_audits,
        //     ...response.data.OPEN.outstanding_audits,
        //   ];
        // });

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
          setTenantAudits(reportInfoArray);
        }
      } catch (err) {
        console.log(err);
      }
    }
    getResponse();
  }, []);

  const handleChecklistClick = () => {
    setOpenChecklist(!openChecklist);
  };
  const handlePrevAuditsClick = () => {
    setOpenPrevAudits(!openPrevAudits);
  };

  return (
    <div>
      {tenantState && tenantAudits ? (
        <>
          <Navbar />
          <Box className={classes.header} textAlign="center" boxShadow={1}>
            <Typography variant="h5">{tenantState.store_name}</Typography>
          </Box>
          <Grid container className={classes.root}>
            <List
              component="nav"
              aria-labelledby="nested-list-subheader"
              className={classes.list}
            >
              {/* <ListItem button divider={true} className={classes.listItem}>
                <ListItemIcon>
                  <QuestionAnswerIcon color="primary" />
                </ListItemIcon>
                <ListItemText primary="View Chats" />
              </ListItem> */}
              <ListItem
                button
                onClick={handlePrevAuditsClick}
                divider={true}
                className={classes.listItem}
              >
                <ListItemIcon>
                  <HistoryIcon color="primary" />
                </ListItemIcon>
                <ListItemText primary="View Previous Audits" />
                {openPrevAudits ? <ExpandLess /> : <ExpandMore />}
              </ListItem>
              <Collapse in={openPrevAudits} timeout="auto" unmountOnExit>
                {tenantAudits.map((audit, index) => {
                  const {
                    open_date,
                    overall_score,
                    report_id,
                    report_type,
                    overall_status,
                  } = audit;
                  return (
                    <>
                      <List component="div" disablePadding>
                        <Box boxShadow={1}>
                          <ListItem className={classes.nested}>
                            <ListItemIcon>
                              <ReceiptIcon color="secondary" />
                            </ListItemIcon>
                            {report_type === "FB" && (
                              <ListItemText
                                primary={`F&B Checklist conducted on ${new Date(
                                  open_date
                                ).toString()}`}
                                secondary={`Score: ${overall_score} `}
                              />
                            )}
                            {report_type === "NFB" && (
                              <ListItemText
                                primary={`Non-F&B Checklist conducted on ${new Date(
                                  open_date
                                ).toString()}`}
                                secondary={`Score: ${overall_score} `}
                              />
                            )}
                            {report_type === "SMA" && (
                              <ListItemText
                                primary={`Safety Management Checklist conducted on ${new Date(
                                  open_date
                                ).toString()}`}
                                secondary={`Score: ${overall_score} `}
                              />
                            )}
                            {overall_status === 1 && (
                              // <ListItemText className={classes.titleResolved}>
                              <Typography
                                variant="button"
                                className={classes.resolvedLabel}
                              >
                                Resolved
                              </Typography>
                              // </ListItemText>
                            )}
                          </ListItem>

                          <div className={classes.previousAuditsButtons}>
                            {overall_status !== 1 && (
                              <Link to={`/tenant/report/${report_id}`}>
                                <Button
                                  variant="contained"
                                  color="secondary"
                                  size="small"
                                  className={classes.button}
                                  startIcon={<ReportProblemIcon />}
                                >
                                  resolve
                                </Button>
                              </Link>
                            )}
                            <Link to={`/tenant/email/${report_id}`}>
                              <Button
                                variant="contained"
                                color="secondary"
                                size="small"
                                className={classes.button}
                                startIcon={<EmailIcon />}
                              >
                                Email
                              </Button>
                            </Link>
                            <Link to={`/fullreport/${report_id}`}>
                              <Button
                                variant="contained"
                                color="secondary"
                                size="small"
                                className={classes.button}
                                startIcon={<CheckCircleIcon />}
                              >
                                report
                              </Button>
                            </Link>
                          </div>
                        </Box>
                      </List>
                    </>
                  );
                })}
              </Collapse>
              <ListItem
                button
                onClick={handleChecklistClick}
                divider={true}
                className={classes.listItem}
              >
                <ListItemIcon>
                  <AssignmentTurnedInIcon color="primary" />
                </ListItemIcon>
                <ListItemText primary="Start Audit" />
                {openChecklist ? <ExpandLess /> : <ExpandMore />}
              </ListItem>

              <Collapse in={openChecklist} timeout="auto" unmountOnExit>
                <List component="div" disablePadding>
                  <Link to={`/tenant/fbChecklist/${tenantId}`}>
                    <ListItem button className={classes.nested}>
                      <ListItemIcon>
                        <FastfoodIcon color="secondary" />
                      </ListItemIcon>
                      <ListItemText primary="Conduct F&B Audit" />
                    </ListItem>
                  </Link>
                  <Link to={`/tenant/nfbChecklist/${tenantId}`}>
                    <ListItem button className={classes.nested}>
                      <ListItemIcon>
                        <StoreIcon color="secondary" />
                      </ListItemIcon>
                      <ListItemText primary="Conduct Non-F&B Audit" />
                    </ListItem>
                  </Link>
                  <Link to={`/tenant/smaChecklist/${tenantId}`}>
                    <ListItem button className={classes.nested}>
                      <ListItemIcon>
                        <LocalHospitalIcon color="secondary" />
                      </ListItemIcon>
                      <ListItemText primary="Conduct Safe Managment Audit" />
                    </ListItem>
                  </Link>
                </List>
              </Collapse>
            </List>
          </Grid>
        </>
      ) : (
        <Loading />
      )}
    </div>
  );
}

export default Tenant;
