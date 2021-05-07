import React, { useState, useContext, useEffect } from "react";
import TextField from "@material-ui/core/TextField";
import { makeStyles } from "@material-ui/core/styles";
import { Context } from "../Context";
import Accordion from "@material-ui/core/Accordion";
import AccordionSummary from "@material-ui/core/AccordionSummary";
import AccordionDetails from "@material-ui/core/AccordionDetails";
import Loading from "../pages/Loading";
import ExpandMoreIcon from "@material-ui/icons/ExpandMore";
import Box from "@material-ui/core/Box";
import ListItem from "@material-ui/core/ListItem";
import ListItemText from "@material-ui/core/ListItemText";
import Avatar from "@material-ui/core/Avatar";
import Button from "@material-ui/core/Button";
import IconButton from "@material-ui/core/IconButton";
import PhotoCamera from "@material-ui/icons/PhotoCamera";
import { Typography } from "@material-ui/core";
import MessageIcon from "@material-ui/icons/Message";
import ListItemIcon from "@material-ui/core/ListItemIcon";

const useStyles = makeStyles((theme) => ({
  mainContainer: {
    display: "flex",
    justifyContent: "center",
  },
  accordion: {
    width: "90%",
    maxWidth: 900,
  },
  titleResolved: {
    padding: theme.spacing(0, 2, 0, 2),
    color: "#F15A22",
  },
  commentBox: {
    maxWidth: 800,
    width: "100%",
    // padding: theme.spacing(0, 0, 2, 2),
  },
  comment: {
    width: "100%",
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
  tenantInstructions: {
    display: "flex",
    padding: theme.spacing(2, 2, 2, 0),
  },

  tenantResponse: {
    display: "flex",
    width: "100%",
    // backgroundColor: theme.palette.background.default,
  },
  avatar: {},
  // button: {
  //   width: "50%",
  //   color: "#F15A22",
  //   fontWeight: "medium",
  //   backgroundColor: theme.palette.background.default,
  // },
  // buttonContainer: {
  //   padding: theme.spacing(4, 0, 4, 0),
  //   display: "flex",
  //   justifyContent: "center",
  // },
  input: {
    display: "none",
  },
  camera: {
    display: "flex",
    flexDirection: "column",
    //   alignItems: "center",
  },
  uploadText: {
    padding: theme.spacing(0, 1, 0, 2),
  },
  image: {
    width: "90%",
    maxWidth: 400,
    // padding: theme.spacing(4, 0, 2, 0),
  },
  buttonSubmit: {
    width: 240,
    // color: "#F15A22",
    fontWeight: "medium",
    backgroundColor: "#F15A22",
  },
  buttonSubmitContainer: {
    padding: theme.spacing(4, 0, 4, 0),
    display: "flex",

    // justifyContent: "center",
  },
  prevResponses: {
    display: "flex",
    flexDirection: "column",
    alignItems: "center",
  },
  prevResponseContainer: {
    margin: theme.spacing(4, 0, 6, 0),
    padding: theme.spacing(5, 5, 5, 5),
    backgroundColor: theme.palette.background.default,
    display: "flex",
    flexDirection: "column",
    alignItems: "center",
    width: "90%",
    maxWidth: 500,
  },
  prevResponseTitle: {
    display: "flex",

    // padding: theme.spacing(2, 0, 2, 2),
    backgroundColor: theme.palette.background.default,
  },
  prevResponseTitleText: {
    padding: theme.spacing(1, 0, 2, 0),
  },
  prevResponseContent: {
    // padding: theme.spacing(2, 0, 2, 2),
    display: "flex",
    flexDirection: "column",
    alignItems: "center",
  },
  prevResponseText: {
    padding: theme.spacing(2, 0, 4, 0),
  },
}));

function TenantReportCard({
  original_remarks,
  qn_id,
  requirement,
  timeframe,
  report_id,
  tenant_id,
  current_qn_status,
  severity,
  image,
}) {
  const classes = useStyles();
  const [comment, setComment] = useState("");
  //selected file state
  // const [selectedFile, setSelectedFile] = useState();
  const [imageState, setImageState] = useState([]);
  const [tenantResponse, setTenantResponse] = useState();

  //state to check if file is selected
  // const [isFilePicked, setIsFilePicked] = useState(false);

  const {
    getReportEntry,
    submitReportUpdate,
    getTenantRectification,
  } = useContext(Context);

  //call when component mounts
  useEffect(() => {
    async function getResponse() {
      try {
        //GET REPORT TYPE

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
  }, []);

  //function to update comment state
  const handleComment = (e) => {
    setComment(e.target.value);
  };

  //function to handle input file change
  const handleChange = (e) => {
    const getBase64 = (file) => {
      return new Promise((resolve, reject) => {
        const reader = new FileReader();

        if (file) {
          reader.readAsDataURL(file);
        }

        reader.onload = () => resolve(reader.result);
        reader.onerror = (error) => reject(error);
      });
    };

    getBase64(e.target.files[0]).then((image) => {
      setImageState(image);
    });
  };

  const createEntry = (
    questionId,
    currentStatus,
    image,
    remark,
    severityLevel
  ) => {
    let array = [];
    array.push({
      qn_id: questionId,
      status: currentStatus === "PASS" ? true : false,
      severity: severityLevel,
      remarks: remark,
      images: image,
    });
    return array;
  };

  //handle submit
  const handleSubmit = () => {
    // console.log(testState);
    // console.log(imageState);

    async function submitAsync() {
      const entry = await createEntry(
        qn_id,
        current_qn_status,
        [imageState],
        comment,
        severity
      );
      console.log(entry);
      // console.log(imageState);

      submitReportUpdate(report_id, false, "", entry[0]);

      alert("Rectification submitted. Pending Approval");
    }
    submitAsync();

    // async function getEntry() {
    //   const entry = await getReportEntry(report_id, entry_id).then(
    //     (response) => {
    //       console.log(response.data);
    //       return response.data;
    //     }
    //   );

    // submitReportUpdate(report_id, false, "", {
    //   ...entry,
    //   images: [imageState],
    //   remarks: comment,
    // });

    // alert("Rectification submitted. Pending Approval");
    // }
    // getEntry();
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
            // id={`additional-actions1-header${entry_id}`}
          >
            <ListItem>
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
                REMARKS: {original_remarks}
              </Typography>

              <Typography
                color="textSecondary"
                variant="button"
                className={classes.textInfo}
              >
                RECTIFICATION PERIOD: {timeframe}
              </Typography>
              {image && (
                <img src={image} className={classes.imageFromAuditor} />
              )}
            </div>

            {current_qn_status === "FAIL" && (
              <>
                <div className={classes.tenantInstructions}>
                  <Avatar src="/broken-image.jpg" className={classes.avatar} />
                  <Typography
                    color="textPrimary"
                    className={classes.textTenant}
                  >
                    Please respond to non-compliance below:
                  </Typography>
                </div>
                <div className={classes.tenantResponse}>
                  <TextField
                    id="standard-multiline-static"
                    placeholder="Comment on rectification to non-compliance"
                    multiline
                    value={comment}
                    className={classes.comment}
                    onChange={(e) => {
                      handleComment(e);
                    }}
                  />
                  <input
                    // accept="image/*"
                    className={classes.input}
                    id={`icon-button-file${qn_id}`}
                    // id="icon-button-file"
                    name={`file${qn_id}`}
                    type="file"
                    // value={null}
                    // name="picture"
                    onClick={(e) => {
                      e.target.value = "";
                    }}
                    onChange={(e) => {
                      handleChange(e);
                    }}
                  />
                  <label
                    htmlFor={`icon-button-file${qn_id}`}
                    // htmlFor="icon-button-file"
                    className={classes.camera}
                  >
                    <IconButton
                      color="primary"
                      aria-label="upload picture"
                      component="span"
                    >
                      <Typography
                        variant="button"
                        className={classes.uploadText}
                      >
                        Upload photo
                      </Typography>
                      <PhotoCamera />
                    </IconButton>
                  </label>
                </div>
                <div className={classes.buttonSubmitContainer}>
                  <Button
                    className={classes.buttonSubmit}
                    variant="contained"
                    color="primary"
                    size="large"
                    onClick={() => {
                      handleSubmit();
                    }}
                    // color="secondary"
                  >
                    submit
                  </Button>
                </div>{" "}
              </>
            )}
            {tenantResponse.map((response) => {
              const { remarks, images } = response;

              return (
                <div className={classes.prevResponses}>
                  <Box className={classes.prevResponseContainer} boxShadow={3}>
                    <div className={classes.prevResponseTitle}>
                      <Avatar
                        src="/broken-image.jpg"
                        className={classes.avatar}
                      />
                      <ListItemIcon>
                        <MessageIcon color="primary" />
                      </ListItemIcon>
                      <Typography
                        color="textPrimary"
                        className={classes.prevResponseTitleText}
                      >
                        Your Previous Response:
                      </Typography>
                    </div>
                    <div className={classes.prevResponseContent}>
                      <Typography
                        color="textPrimary"
                        // variant="h8"
                        className={classes.prevResponseText}
                        variant="caption"
                      >
                        {remarks}
                      </Typography>
                      {images.length !== 0 && (
                        <img src={images[0]} className={classes.image}></img>
                      )}
                    </div>
                  </Box>
                </div>
              );
            })}
          </AccordionDetails>
        </Accordion>
      ) : (
        <Loading />
      )}
    </div>
  );
}

export default TenantReportCard;
