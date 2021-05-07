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
  const {
    getTenantAudits,
    getUserInfoNoParams,
    getReport,
    tenantState,
    setTenantState,
  } = useContext(Context);
  const classes = useStyles();
  const [value, setValue] = React.useState(0);

  const handleChange = (event, newValue) => {
    setValue(newValue);
  };

  const filterAudits = (category) => {
    async function getResponse() {
      try {
        const tenantId = await getUserInfoNoParams().then((response) => {
          //   console.log(response);
          return response.data.acc_id;
        });
        // console.log(tenantId);
        if (category === "OVERDUE") {
          const reportIdArray = await getTenantAudits(tenantId).then(
            (response) => {
              // console.log(response);
              if (response.data.OVERDUE === -1) {
                return [];
              }
              return [response.data.OVERDUE];
            }
          );
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
            setTenantState(reportInfoArray);
          }
        }
        if (category === "UNRESOLVED") {
          const reportIdArray = await getTenantAudits(tenantId).then(
            (response) => {
              // console.log(response);
              if (response.data.LATEST === -1) {
                return [];
              }
              return [response.data.LATEST];
            }
          );
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
            setTenantState(reportInfoArray);
          }
        }
        if (category === "COMPLETED") {
          const reportIdArray = await getTenantAudits(tenantId).then(
            (response) => {
              if (response.data.LATEST === -1) {
                return [...response.data.CLOSED.past_audits];
              }
              return [
                response.data.LATEST,
                ...response.data.CLOSED.past_audits,
              ];
            }
          );
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
            setTenantState(reportInfoArray);
          }
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
