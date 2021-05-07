import React, { useContext } from "react";
import { makeStyles, withStyles } from "@material-ui/core/styles";
import Paper from "@material-ui/core/Paper";
import Tabs from "@material-ui/core/Tabs";
import Tab from "@material-ui/core/Tab";
import { Context } from "../Context";

const useStyles = makeStyles((theme) => ({
  //   root: { },
}));

const StyledTabs = withStyles({
  indicator: {
    display: "flex",
    justifyContent: "center",
    backgroundColor: "transparent",
    "& > span": {
      maxWidth: 40,
      width: "100%",
      backgroundColor: "#F15A22",
    },
  },
})((props) => <Tabs {...props} TabIndicatorProps={{ children: <span /> }} />);

const StyledTab = withStyles((theme) => ({
  root: {
    flexGrow: 1,
    textTransform: "none",
    color: "#000000",
    fontWeight: theme.typography.fontWeightBold,
    fontSize: theme.typography.pxToRem(16),
    margin: theme.spacing(2, 0, 2, 0),
    "&:focus": {
      opacity: 1,
    },
  },
}))((props) => <Tab disableRipple {...props} />);

export default function CenteredTabs() {
  const { getAudits, setAuditsState, getReport } = useContext(Context);
  const classes = useStyles();
  const [value, setValue] = React.useState(0);

  const handleChange = (event, newValue) => {
    setValue(newValue);
  };

  const filterAudits = (category) => {
    async function getResponse() {
      try {
        const username = sessionStorage.getItem("authenticatedUser");
        if (category === "COMPLETED") {
          const reportIdArray = await getAudits(username).then((response) => {
            return [
              ...response.data.CLOSED.completed_audits,
              ...response.data.OPEN.outstanding_audits,
            ];
          });
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

          //set state of audits to be an array of report info objects
          setAuditsState(reportInfoArray);
        }
        if (category === "UNRESOLVED") {
          const reportIdArray = await getAudits(username).then((response) => {
            return [...response.data.OPEN.outstanding_audits];
          });
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

          //set state of audits to be an array of report info objects
          setAuditsState(reportInfoArray);
        }
        if (category === "OVERDUE") {
          const reportIdArray = await getAudits(username).then((response) => {
            console.log(response);
            return response.data.OVERDUE;
          });
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

          //set state of audits to be an array of report info objects
          setAuditsState(reportInfoArray);
        }
      } catch (err) {
        console.log(err);
      }
    }
    getResponse();
  };

  return (
    <Paper className={classes.root}>
      <StyledTabs value={value} onChange={handleChange} centered>
        <StyledTab
          onClick={() => {
            filterAudits("COMPLETED");
          }}
          label="COMPLETED"
        />
        <StyledTab
          onClick={() => {
            filterAudits("UNRESOLVED");
          }}
          label="UNRESOLVED"
        />
        <StyledTab
          onClick={() => {
            filterAudits("OVERDUE");
          }}
          label="OVERDUE"
        />
      </StyledTabs>
    </Paper>
  );
}
