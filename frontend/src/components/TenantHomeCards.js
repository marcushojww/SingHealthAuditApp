import React, { useContext, useEffect, useState } from "react";
import AppBar from "@material-ui/core/AppBar";
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
import { Link } from "react-router-dom";
import { Context } from "../Context";
import { tenantImages } from "../data";
import zIndex from "@material-ui/core/styles/zIndex";

const useStyles = makeStyles((theme) => ({
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
}));
export default function TenantHomeCards() {
  const classes = useStyles();

  const { tenantState } = useContext(Context);

  return (
    <React.Fragment>
      <Container className={classes.cardGrid} maxWidth="md">
        {/* End hero unit */}
        <Grid container spacing={4}>
          {tenantState.map((audit, index) => {
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

            return (
              <Grid item key={index} xs={12} sm={6} md={4}>
                <Card className={classes.card}>
                  <CardMedia
                    component="img"
                    alt="tenant-image"
                    className={classes.cardMedia}
                    // image={`${imageObject.imageUrl}`}
                    image={
                      imageObject.imageUrl
                        ? `${imageObject.imageUrl}`
                        : `url(https://source.unsplash.com/random)`
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
                      <Link to={`/t/report/${report_id}`}>
                        <Button
                          size="medium"
                          color="primary"
                          className={classes.button}
                        >
                          Rectify
                        </Button>
                      </Link>
                    )}
                    <Link to={`/fullreport/${report_id}`}>
                      <Button
                        size="medium"
                        color="primary"
                        className={classes.button}
                      >
                        View Report
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
