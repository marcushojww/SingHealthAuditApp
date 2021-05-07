import React from "react";
import Accordion from "@material-ui/core/Accordion";
import AccordionSummary from "@material-ui/core/AccordionSummary";
import AccordionDetails from "@material-ui/core/AccordionDetails";
import { makeStyles } from "@material-ui/core/styles";
import ExpandMoreIcon from "@material-ui/icons/ExpandMore";
import ListItem from "@material-ui/core/ListItem";
import ListItemText from "@material-ui/core/ListItemText";
import { Typography } from "@material-ui/core";

const useStyles = makeStyles((theme) => ({
  dropdownContainer: {
    display: "flex",
    flexDirection: "column",
    // alignItems: "center",
    padding: theme.spacing(0, 0, 3, 8),
    // color: "#F15A22",
  },
  imageFromAuditor: {
    width: "70%",
    maxWidth: 400,
    padding: theme.spacing(2, 0, 0, 0),
  },
  passedLabel: {
    color: "#03A762",
  },
  failedLabel: {
    color: "#F15A22",
  },
}));

function ReportQuestion({
  qn_id,
  requirement,
  images,
  remarks,
  status,
  severity,
}) {
  const classes = useStyles();

  return (
    <div>
      <Accordion>
        <AccordionSummary
          expandIcon={<ExpandMoreIcon />}
          aria-label="Expand"
          aria-controls="additional-actions1-content"
          id="additional-actions1-header"
        >
          <ListItem>
            <ListItemText id={qn_id} primary={requirement}></ListItemText>
          </ListItem>
          {status === "PASS" && (
            // <ListItemText className={classes.titleResolved}>
            <Typography variant="button" className={classes.passedLabel}>
              PASSED
            </Typography>
            // </ListItemText>
          )}
          {status === "FAIL" && (
            // <ListItemText className={classes.titleResolved}>
            <Typography variant="button" className={classes.failedLabel}>
              FAILED
            </Typography>
            // </ListItemText>
          )}
        </AccordionSummary>
        <AccordionDetails className={classes.dropdownContainer}>
          <Typography variant="button">Remarks : {remarks}</Typography>
          <Typography variant="button">Severity : {severity}</Typography>
          {images && <img src={images} className={classes.imageFromAuditor} />}
        </AccordionDetails>
      </Accordion>
    </div>
  );
}

export default ReportQuestion;
