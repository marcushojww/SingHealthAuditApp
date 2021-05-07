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
    margin: theme.spacing(4, 2, 10, 2),
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
  },
}));

function Store() {
  const [openChecklist, setOpenChecklist] = useState(false);
  const [openPrevAudits, setOpenPrevAudits] = useState(false);
  const [tenantInfo, setTenantInfo] = useState();
  //Context: getUserInfo method
  const {
    tenantState,
    getUserInfoNoParams,
    getTenantAudits,
    getReport,
    setTenantState,
  } = useContext(Context);

  const classes = useStyles();

  useEffect(() => {
    async function getResponse() {
      try {
        const tenantId = await getUserInfoNoParams().then((response) => {
          console.log(response);
          setTenantInfo(response.data);
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

  const handleChecklistClick = () => {
    setOpenChecklist(!openChecklist);
  };
  const handlePrevAuditsClick = () => {
    setOpenPrevAudits(!openPrevAudits);
  };

  return (
    <div>
      {tenantState && tenantInfo ? (
        <>
          <Navbar />
          <Box className={classes.header} textAlign="center" boxShadow={1}>
            <Typography variant="h5">{tenantInfo.store_name}</Typography>
          </Box>
          <div className={classes.root}>
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
                  <AssignmentTurnedInIcon color="primary" />
                </ListItemIcon>
                <ListItemText primary="View Audits" />
                {openPrevAudits ? <ExpandLess /> : <ExpandMore />}
              </ListItem>
              <Collapse in={openPrevAudits} timeout="auto" unmountOnExit>
                {tenantState.map((audit, index) => {
                  const {
                    open_date,
                    overall_score,
                    report_id,
                    report_type,
                    overall_status,
                  } = audit;
                  return (
                    <List component="div" disablePadding>
                      <Box boxShadow={1}>
                        <ListItem button className={classes.nested}>
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
                          {report_type === "SMA" && (
                            <ListItemText
                              primary={`Safety Management Checklist conducted on ${new Date(
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
                            <Link to={`/t/report/${report_id}`}>
                              <Button
                                variant="contained"
                                color="secondary"
                                size="small"
                                className={classes.button}
                                startIcon={<ReportProblemIcon />}
                              >
                                Rectify
                              </Button>
                            </Link>
                          )}
                          <Link to={`/fullreport/${report_id}`}>
                            <Button
                              variant="contained"
                              color="secondary"
                              size="small"
                              className={classes.button}
                              startIcon={<CheckCircleIcon />}
                            >
                              View Report
                            </Button>
                          </Link>
                        </div>
                      </Box>
                    </List>
                  );
                })}
              </Collapse>
            </List>
          </div>
        </>
      ) : (
        <Loading />
      )}
    </div>
  );
}
export default Store;
