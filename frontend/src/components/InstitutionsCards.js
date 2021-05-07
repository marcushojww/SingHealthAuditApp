import React from "react";
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
import { institutions } from "../data";
import { Link } from "react-router-dom";

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
    // width: "100%",
    padding: theme.spacing(2, 2, 2, 2),

    // paddingTop: "56.25%",
    // height: "0%",
    // height: "100%",
  },
  cardContent: {
    flexGrow: 1,
  },
  button: {
    // color: "#F15A22",
    // fontWeight: "medium",
  },
  // footer: {
  //   backgroundColor: theme.palette.background.paper,
  //   padding: theme.spacing(6),
  // },
}));

export default function HomeAuditorCards() {
  const classes = useStyles();

  return (
    <React.Fragment>
      {/* <AppBar position="relative">
        <Toolbar>
          <CameraIcon className={classes.icon} />
          <Typography variant="h6" color="inherit" noWrap>
            Album layout
          </Typography>
        </Toolbar>
      </AppBar> */}

      <Container className={classes.cardGrid} maxWidth="md">
        {/* End hero unit */}
        <Grid container spacing={4}>
          {institutions.map((institution, index) => {
            const { name, imageUrl } = institution;
            console.log(institution);
            return (
              <Grid item key={index} xs={12} sm={6} md={4}>
                <Card className={classes.card}>
                  <CardMedia
                    component="img"
                    alt="institution-image"
                    className={classes.cardMedia}
                    image={`${imageUrl}`}
                    title="Image title"
                  />
                  <CardContent className={classes.cardContent}>
                    <Typography gutterBottom variant="h5" component="h2">
                      {name}
                    </Typography>
                    <Typography variant="caption">
                      Address: {institution.address}
                    </Typography>
                  </CardContent>
                  <CardActions>
                    <Link to={`/institution/${name}`}>
                      <Button
                        className={classes.button}
                        size="medium"
                        color="primary"
                        // color="secondary"
                      >
                        View Tenants
                      </Button>
                    </Link>
                  </CardActions>
                </Card>
              </Grid>
            );
          })}
        </Grid>
      </Container>

      {/* Footer */}
      {/* <footer className={classes.footer}>
        <Typography variant="h6" align="center" gutterBottom>
          Footer
        </Typography>
        <Typography
          variant="subtitle1"
          align="center"
          color="textSecondary"
          component="p"
        >
          Something here to give the footer a purpose!
        </Typography>
        <Copyright />
      </footer> */}
      {/* End footer */}
    </React.Fragment>
  );
}
