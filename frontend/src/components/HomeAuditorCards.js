import React, { useContext, useEffect, useState } from "react";
import AppBar from "@material-ui/core/AppBar";
import { Link } from "react-router-dom";
import Button from "@material-ui/core/Button";
import CameraIcon from "@material-ui/icons/PhotoCamera";
import Card from "@material-ui/core/Card";
import CardActions from "@material-ui/core/CardActions";
import CardContent from "@material-ui/core/CardContent";
import CardMedia from "@material-ui/core/CardMedia";
import Grid from "@material-ui/core/Grid";
import Toolbar from "@material-ui/core/Toolbar";
import Typography from "@material-ui/core/Typography";
import { makeStyles } from "@material-ui/core/styles";
import Container from "@material-ui/core/Container";
// import Link from "@material-ui/core/Link";
import { Context } from "../Context";
import { tenantImages } from "../data";
import zIndex from "@material-ui/core/styles/zIndex";
import defaultShop from "../images/defaultshop.jpeg";

// function Copyright() {
//   return (
//     <Typography variant="body2" color="textSecondary" align="center">
//       {"Copyright © "}
//       <Link color="inherit" href="https://material-ui.com/">
//         Your Website
//       </Link>{" "}
//       {new Date().getFullYear()}
//       {"."}
//     </Typography>
//   );
// }

const useStyles = makeStyles((theme) => ({
  // icon: {
  //   marginRight: theme.spacing(2),
  // },
  // heroContent: {
  //   backgroundColor: theme.palette.background.paper,
  //   padding: theme.spacing(8, 0, 6),
  // },
  // heroButtons: {
  //   // marginTop: theme.spacing(4),
  // },
  cardGrid: {
    paddingTop: theme.spacing(8),
    paddingBottom: theme.spacing(8),
  },
  card: {
    height: "100%",
    display: "flex",
    flexDirection: "column",
  },
  cardMedia: {
    // padding: theme.spacing(2, 2, 2, 2),
    height: 150,
    width: "100%",
    // paddingTop: "56.25%", // 16:9
  },
  cardContent: {
    flexGrow: 1,
  },
  cardTitle: {
    display: "flex",
    // justifyContent: "space-between",
  },
  resolvedLabel: {
    color: "#F15A22",
    padding: theme.spacing(0.5, 2, 0, 2),
  },
  button: {
    // color: "#F15A22",
  },
  // footer: {
  //   backgroundColor: theme.palette.background.paper,
  //   padding: theme.spacing(6),
  // },
}));

export default function HomeAuditorCards() {
  const classes = useStyles();

  const { auditsState } = useContext(Context);

  return (
    <React.Fragment>
      <Container className={classes.cardGrid} maxWidth="md">
        {/* End hero unit */}
        <Grid container spacing={4}>
          {auditsState.map((audit, index) => {
            console.log(audit);
            const {
              store_name,
              open_date,
              overall_status,
              overall_score,
              report_type,
              report_id,
            } = audit;

            let imageObject = tenantImages.find(
              (image) => image.name === store_name
            );
            console.log(imageObject);
            return (
              <Grid item key={index} xs={12} sm={6} md={4}>
                <Card className={classes.card}>
                  <CardMedia
                    component="img"
                    alt="tenant-image"
                    className={classes.cardMedia}
                    // image={`${imageObject.imageUrl}`}
                    image={
                      imageObject !== undefined
                        ? `${imageObject.imageUrl}`
                        : `${defaultShop}`
                    }
                    title="Image title"
                  />
                  <CardContent className={classes.cardContent}>
                    <div className={classes.cardTitle}>
                      <Typography gutterBottom variant="h5" component="h2">
                        {store_name}
                      </Typography>
                      {overall_status === 1 && (
                        <Typography
                          variant="button"
                          className={classes.resolvedLabel}
                        >
                          Resolved
                        </Typography>
                      )}
                    </div>

                    <Typography variant="caption">
                      <div>Date: {new Date(open_date).toString()}</div>
                      {report_type === "FB" && <div>Type: Food & Beverage</div>}
                      {report_type === "NFB" && (
                        <div>Type: Non-Food & Beverage</div>
                      )}
                      {report_type === "SMA" && (
                        <div>Type: Safe Management</div>
                      )}
                      <div>Score: {overall_score}</div>
                    </Typography>
                  </CardContent>
                  <CardActions>
                    {overall_status === 0 && (
                      <Link to={`/tenant/report/${report_id}`}>
                        <Button
                          size="medium"
                          color="primary"
                          className={classes.button}
                        >
                          resolve
                        </Button>
                      </Link>
                    )}
                    <Link to={`/tenant/email/${report_id}`}>
                      <Button
                        size="medium"
                        color="primary"
                        className={classes.button}
                      >
                        email
                      </Button>
                    </Link>
                    <Link to={`/fullreport/${report_id}`}>
                      <Button
                        size="medium"
                        color="primary"
                        className={classes.button}
                      >
                        report
                      </Button>
                    </Link>
                  </CardActions>
                </Card>
              </Grid>
            );
          })}
        </Grid>
      </Container>
    </React.Fragment>
  );
}
