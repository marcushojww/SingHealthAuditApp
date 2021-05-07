import React from "react";
import { makeStyles } from "@material-ui/core/styles";
import CircularProgress from "@material-ui/core/CircularProgress";
import Typography from "@material-ui/core/Typography";
import Box from "@material-ui/core/Box";

const useStyles = makeStyles((theme) => ({
  root: {
    // display: "flex",
    // "& > * + *": {
    //   marginLeft: theme.spacing(2),
    // },
    // justifyContent: "center",
    position: "fixed",
    top: "40%",
    left: "40%",
  },
  loading: {
    color: "#F15A22",
  },
}));

export default function CircularIndeterminate() {
  const classes = useStyles();

  return (
    <div className={classes.root}>
      <Box textAlign="center">
        <Typography variant="h5" className={classes.text}>
          Loading
        </Typography>
        <CircularProgress className={classes.loading} />
      </Box>
    </div>
  );
}
