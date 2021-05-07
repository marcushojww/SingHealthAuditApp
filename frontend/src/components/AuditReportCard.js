import React, { useState, useEffect, useContext } from "react";
import { makeStyles } from "@material-ui/core/styles";
import { Context } from "../Context";
import Accordion from "@material-ui/core/Accordion";
import AccordionSummary from "@material-ui/core/AccordionSummary";
import AccordionDetails from "@material-ui/core/AccordionDetails";
import Checkbox from "@material-ui/core/Checkbox";
import FormControlLabel from "@material-ui/core/FormControlLabel";
import ExpandMoreIcon from "@material-ui/icons/ExpandMore";
import List from "@material-ui/core/List";
import ListItem from "@material-ui/core/ListItem";
import ListItemText from "@material-ui/core/ListItemText";
import Avatar from "@material-ui/core/Avatar";
import Typography from "@material-ui/core/Typography";
import Button from "@material-ui/core/Button";
import Box from "@material-ui/core/Box";
import Loading from "../pages/Loading";

const useStyles = makeStyles((theme) => ({
  mainContainer: {
    display: "flex",
    justifyContent: "center",
  },
  accordion: {
    width: "90%",
    maxWidth: 900,
  },
  title: {
    display: "flex",
    flexDirection: "row",
  },
  titleResolved: {
    padding: theme.spacing(0, 2, 0, 2),
    color: "#F15A22",
  },

  dropdownMain: {
    display: "flex",
    flexDirection: "column",
    padding: theme.spacing(0, 6, 0, 6),
  },
  dropdownContainer: {
    display: "flex",
    flexDirection: "column",
    // margin: theme.spacing(0, 0, 0, 2),
  },
  imageFromAuditor: {
    width: "70%",
    maxWidth: 400,
    padding: theme.spacing(0, 0, 3, 0),
  },
  textInfo: {
    padding: theme.spacing(2, 0, 2, 0),
    color: "#F15A22",
  },
  topText: {
    color: "#F15A22",
  },
  textTenant: {
    padding: theme.spacing(1, 0, 2, 2),
    fontWeight: "medium",
  },
  tenantResponses: {
    display: "flex",
    flexDirection: "column",
    alignItems: "center",
  },
  tenantResponseContainer: {
    margin: theme.spacing(4, 0, 6, 0),
    padding: theme.spacing(5, 5, 5, 5),
    backgroundColor: theme.palette.background.default,
    display: "flex",
    flexDirection: "column",
    alignItems: "center",
    width: "90%",
    maxWidth: 500,
  },
  // tenantResponse: {
  //   display: "flex",
  //   padding: theme.spacing(2, 0, 2, 2),
  //   backgroundColor: theme.palette.background.default,
  // },
  tenantResponseTitle: {
    display: "flex",
  },
  tenantResponseContent: {
    display: "flex",
    flexDirection: "column",
    alignItems: "center",
  },
  tenantTextResponse: {
    padding: theme.spacing(2, 0, 4, 0),
  },

  avatar: {},
  image: {
    width: "90%",
    maxWidth: 400,
    // padding: theme.spacing(3, 2, 2, 2),
  },
  button: {
    width: 240,
    // color: "#F15A22",
    fontWeight: "medium",
    backgroundColor: "#F15A22",
  },
  buttonContainer: {
    padding: theme.spacing(2, 0, 8, 0),
    display: "flex",
    justifyContent: "center",
    // justifyContent: "center",
  },
  // media: {
  //   width: 100,
  //   height: 100,
  //   backgroundColor: theme.palette.background.default,
  // },
}));

function AuditReportCard({
  current_qn_status,
  requirement,
  original_remarks,
  timeframe,
  report_id,
  tenant_id,
  qn_id,
  image,
}) {
  const classes = useStyles();

  //state of tenant rectification
  // const [tenantRemarks, setTenantRemarks] = useState();
  // const [tenantRectificationImage, setTenantRectificationImage] = useState();
  // const [failedEntries, setFailedEntries] = useState();
  const [tenantResponse, setTenantResponse] = useState();

  const {
    getTenantRectification,
    submitReportUpdate,
    resolvedState,
    setResolvedState,
  } = useContext(Context);

  useEffect(() => {
    async function getResponse() {
      try {
        //GET TENANT RECTIFICATION RESPONSES
        getTenantRectification(report_id, tenant_id, qn_id)
          .then((response) => {
            console.log(response.data.entries);

            setTenantResponse(response.data.entries);
          })
          .catch((error) => {
            console.log(error);
          });
        // console.log(tenantEntry);
      } catch (error) {
        console.log(error);
      }
    }
    getResponse();
  }, [resolvedState]);

  const resolveNonCompliance = () => {
    alert("Non-compliance successfully resolved");
    async function resolveAsync() {
      // const entry = await getReportEntry(report_id, qn_id).then((response) => {
      //   console.log(response.data);
      //   return response.data;
      // });
      submitReportUpdate(report_id, false, "", {
        qn_id: qn_id,
        status: true,
      });
    }
    resolveAsync();
  };

  return (
    <div className={classes.mainContainer}>
      {tenantResponse ? (
        <Accordion className={classes.accordion}>
          <AccordionSummary
            expandIcon={<ExpandMoreIcon />}
            aria-label="Expand"
            aria-controls="additional-actions1-content"
            id="additional-actions1-header"
          >
            <ListItem className={classes.title}>
              <ListItemText id={qn_id} primary={requirement} />
              {current_qn_status === "PASS" && (
                <ListItemText className={classes.titleResolved}>
                  <Typography variant="button">Resolved</Typography>
                </ListItemText>
              )}
            </ListItem>
          </AccordionSummary>
          <AccordionDetails className={classes.dropdownMain}>
            <div className={classes.dropdownContainer}>
              <Typography
                color="textSecondary"
                variant="button"
                className={classes.topText}
              >
                YOUR REMARKS: {original_remarks}
              </Typography>

              <Typography
                color="textSecondary"
                variant="button"
                className={classes.textInfo}
              >
                RECTIFICATION PERIOD: {timeframe}
              </Typography>
              {image && (
                <img src={image} className={classes.imageFromAuditor}></img>
              )}
            </div>
            <div className={classes.tenantResponses}>
              {tenantResponse.map((response) => {
                const { remarks, images } = response;

                return (
                  <>
                    <Box
                      className={classes.tenantResponseContainer}
                      boxShadow={2}
                    >
                      <div className={classes.tenantResponseTitle}>
                        <Avatar
                          src="/broken-image.jpg"
                          className={classes.avatar}
                        />
                        <Typography
                          color="textPrimary"
                          className={classes.textTenant}
                        >
                          Tenant Response:
                        </Typography>
                      </div>
                      <div className={classes.tenantResponseContent}>
                        <Typography
                          color="textPrimary"
                          // variant="h8"
                          className={classes.tenantTextResponse}
                          variant="caption"
                        >
                          {remarks}
                        </Typography>

                        {images.length !== 0 && (
                          <img src={images[0]} className={classes.image}></img>
                        )}
                      </div>
                    </Box>
                  </>
                );
              })}
            </div>
            {current_qn_status === "FAIL" && (
              <div className={classes.buttonContainer}>
                <Button
                  variant="contained"
                  color="primary"
                  className={classes.button}
                  size="large"
                  onClick={() => {
                    resolveNonCompliance();
                  }}
                  // color="secondary"
                >
                  resolve
                </Button>
              </div>
            )}
          </AccordionDetails>
        </Accordion>
      ) : (
        <Loading />
      )}
    </div>
  );
}

export default AuditReportCard;
