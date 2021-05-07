import React, { useContext, useState } from "react";
import TextField from "@material-ui/core/TextField";
import Accordion from "@material-ui/core/Accordion";
import AccordionSummary from "@material-ui/core/AccordionSummary";
import AccordionDetails from "@material-ui/core/AccordionDetails";
import List from "@material-ui/core/List";
import ListItem from "@material-ui/core/ListItem";
import ListItemText from "@material-ui/core/ListItemText";
import ListItemAvatar from "@material-ui/core/ListItemAvatar";
import Checkbox from "@material-ui/core/Checkbox";
import Avatar from "@material-ui/core/Avatar";
import Button from "@material-ui/core/Button";
import { FaRegEdit } from "react-icons/fa";
import { Context } from "../Context";
import { makeStyles } from "@material-ui/core/styles";
import ExpandMoreIcon from "@material-ui/icons/ExpandMore";
import SentimentVeryDissatisfiedIcon from "@material-ui/icons/SentimentVeryDissatisfied";
import Box from "@material-ui/core/Box";
import Typography from "@material-ui/core/Typography";
import PropTypes from "prop-types";
import Rating from "@material-ui/lab/Rating";
import { withStyles } from "@material-ui/core/styles";
import IconButton from "@material-ui/core/IconButton";
import PhotoCamera from "@material-ui/icons/PhotoCamera";

const useStyles = makeStyles((theme) => ({
  dropdownContainer: {
    display: "flex",
    flexDirection: "column",
    alignItems: "center",
  },
  dropdownSection: {
    display: "flex",
    justifyContent: "space-around",
    width: "100%",
    // justifyContent: "center",
  },
  commentBox: {
    // margin: theme.spacing(0, 2, 0, 2),
    width: "70%",
  },
  button: {
    fontWeight: "medium",
    backgroundColor: "#F15A22",
  },
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
  buttonContainer: {
    padding: theme.spacing(2, 0, 2, 0),
  },
}));
const StyledRating = withStyles({
  iconFilled: {
    color: "#ff6d75",
  },
  iconHover: {
    color: "#ff3d47",
  },
})(Rating);

const customIcons = {
  1: {
    icon: <SentimentVeryDissatisfiedIcon />,
    label: "Very Dissatisfied",
  },
  2: {
    icon: <SentimentVeryDissatisfiedIcon />,
    label: "Dissatisfied",
  },
  3: {
    icon: <SentimentVeryDissatisfiedIcon />,
    label: "Neutral",
  },
  4: {
    icon: <SentimentVeryDissatisfiedIcon />,
    label: "Satisfied",
  },
  5: {
    icon: <SentimentVeryDissatisfiedIcon />,
    label: "Very Satisfied",
  },
};

function IconContainer(props) {
  const { value, ...other } = props;
  return <span {...other}>{customIcons[value].icon}</span>;
}

IconContainer.propTypes = {
  value: PropTypes.number.isRequired,
};

function Question({ fb_qn_id, requirement, labelId, type }) {
  const classes = useStyles();

  const {
    fbReportState,
    setFbReportState,
    nfbReportState,
    setNonFbReportState,
    smaReportState,
    setSMAReportState,
  } = useContext(Context);

  //state to update number of checked boxes
  const [checked, setChecked] = useState([]);
  //state to update comments
  const [comment, setComment] = useState("");
  //state to update severity
  const [severity, setSeverity] = useState(0);
  //state to update image
  const [imageState, setImageState] = useState([]);

  //handle the checkbox changes
  const handleToggle = (question_id) => () => {
    // current question id
    const currentIndex = checked.indexOf(question_id);

    const newChecked = [...checked];

    if (currentIndex === -1) {
      newChecked.push(question_id);
    } else {
      newChecked.splice(currentIndex, 1);
    }
    // update the checked state
    setChecked(newChecked);

    //update report state depending on the type

    switch (type) {
      case "FB":
        setFbReportState((prevState) => {
          return prevState.map((question) =>
            question.qn_id === question_id
              ? { ...question, status: !question.status }
              : question
          );
        });
        break;
      case "NFB":
        setNonFbReportState((prevState) => {
          return prevState.map((question) =>
            question.qn_id === question_id
              ? { ...question, status: !question.status }
              : question
          );
        });
        break;
      case "SMA":
        setSMAReportState((prevState) => {
          return prevState.map((question) =>
            question.qn_id === question_id
              ? { ...question, status: !question.status }
              : question
          );
        });
        break;
      default:
        console.log("Invalid type of checklist");
        break;
    }
  };
  //function to update comment state
  const handleComment = (e) => {
    setComment(e.target.value);
  };
  //function to update severity state
  const handleSeverity = (e) => {
    setSeverity(e.target.value);
    // console.log(e.target.value);
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
      console.log(image);
      setImageState(image);
    });
  };

  //function to update fb report state upon clicking save
  const handleSave = () => {
    alert(`Input saved for question ${parseInt(fb_qn_id)}`);
    let today = new Date();
    switch (severity) {
      case "1":
        today.setDate(today.getDate() + 21);
        break;
      case "2":
        today.setDate(today.getDate() + 14);
        break;
      case "3":
        today.setDate(today.getDate() + 7);
        break;
      case "4":
        today.setDate(today.getDate() + 4);
        break;
      case "5":
        today.setDate(today.getDate() + 1);
        break;
      default:
        today.setDate(today.getDate());
        break;
    }

    let severityDate =
      (today.getDate() < 10
        ? "0" + today.getDate().toString()
        : today.getDate().toString()) +
      (today.getMonth() + 1 < 10
        ? "0" + (today.getMonth() + 1).toString()
        : (today.getMonth() + 1).toString()) +
      today.getFullYear().toString().slice(2, 4);

    console.log(parseInt(severity + severityDate));

    switch (type) {
      case "FB":
        setFbReportState((prevState) => {
          return prevState.map((question) =>
            question.qn_id === fb_qn_id && severity !== "0"
              ? {
                  ...question,
                  severity: parseInt(severity + severityDate),
                  remarks: comment,
                  images: [imageState],
                }
              : question
          );
        });
        break;
      case "NFB":
        setNonFbReportState((prevState) => {
          return prevState.map((question) =>
            question.qn_id === fb_qn_id && severity !== "0"
              ? {
                  ...question,
                  severity: parseInt(severity + severityDate),
                  remarks: comment,
                  images: [imageState],
                }
              : question
          );
        });
        break;
      case "SMA":
        setSMAReportState((prevState) => {
          return prevState.map((question) =>
            question.qn_id === fb_qn_id && severity !== "0"
              ? {
                  ...question,
                  severity: parseInt(severity + severityDate),
                  remarks: comment,
                  images: [imageState],
                }
              : question
          );
        });
        break;
      default:
        console.log("Invalid type of checklist");
        break;
    }
  };

  return (
    <div>
      <Accordion>
        <AccordionSummary
          key={fb_qn_id}
          expandIcon={<ExpandMoreIcon />}
          aria-label="Expand"
          aria-controls="additional-actions1-content"
          id="additional-actions1-header"
        >
          <ListItem>
            <ListItemText id={fb_qn_id} primary={`${requirement}`} />
            <Checkbox
              // edge="end"
              onChange={handleToggle(fb_qn_id)}
              checked={checked.indexOf(fb_qn_id) === -1}
              inputProps={{ "aria-labelledby": labelId }}
              className={classes.checkbox}
            />
          </ListItem>
        </AccordionSummary>
        <AccordionDetails className={classes.dropdownContainer}>
          <div className={classes.dropdownSection}>
            <TextField
              id="standard-multiline-static"
              label="Comment on non-compliance"
              multiline
              value={comment}
              onChange={(e) => {
                handleComment(e);
              }}
              className={classes.commentBox}
            />
            <Box component="fieldset" mb={3} borderColor="transparent">
              <Typography component="legend">Set Severity</Typography>
              <Rating
                name={`${fb_qn_id}`}
                defaultValue={0}
                // getLabelText={(value) => customIcons[value].label}
                IconContainerComponent={IconContainer}
                onChange={(e) => {
                  handleSeverity(e);
                }}
              />
            </Box>
            <input
              // accept="image/*"
              className={classes.input}
              id={`icon-button-file${fb_qn_id}`}
              // id="icon-button-file"
              name={`file${fb_qn_id}`}
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
              htmlFor={`icon-button-file${fb_qn_id}`}
              // htmlFor="icon-button-file"
              className={classes.camera}
            >
              <IconButton
                color="primary"
                aria-label="upload picture"
                component="span"
              >
                {/* <Typography variant="button" className={classes.uploadText}>
                  Upload photo
                </Typography> */}
                <PhotoCamera />
              </IconButton>
            </label>
          </div>

          <div className={classes.buttonContainer}>
            <Button
              className={classes.button}
              size="large"
              color="primary"
              variant="contained"
              onClick={() => {
                handleSave();
              }}
              // color="secondary"
            >
              save
            </Button>
          </div>
        </AccordionDetails>
      </Accordion>
    </div>
  );
}

export default Question;
