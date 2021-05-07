import React, { useState, useEffect, useContext } from "react";
import { Typography, Button, FormGroup, FormControlLabel, Checkbox } from '@material-ui/core';
import Favorite from '@material-ui/icons/Favorite';
import FavoriteBorder from '@material-ui/icons/FavoriteBorder';

import { Context } from '../Context';
import Navbar from "../Navbar";
import useStyles from '../styles';

function UI_stuff() {

  const [checkboxState, setCheckboxState] = React.useState({
    checkedA: true,
    checkedB: true,
    checkedF: true,
    checkedG: true,
  });

  const styles = useStyles();

  const handleCheckboxChange = (event) => {
    setCheckboxState({ ...checkboxState, [event.target.name]: event.target.checked });
  };
  return (
    <main>
      {/* <Navbar /> */}
      <div>
        <h2>Material UI test</h2>
        {/* <HomeCategories filterAudits={filterAudits} />
        <Audits homeAudits={homeAudits} /> */}
      </div>
      <Button variant="outlined" className={styles.buttons}>Outlined Button</Button>
      <Button variant="contained" color="primary">Primary Contained Button</Button>
      <Button variant="outlined" color="secondary">Secondary Outlined Button</Button>
      <Button variant="outlined" disabled>Disabled Outlined Button</Button>

      <div className={styles.container}>
        <FormGroup column="true">
          <FormControlLabel
            control={
              <Checkbox 
                checked={checkboxState.checkedA} 
                onChange={handleCheckboxChange}
                name='checkedA'
                color='primary'
              />
            }
            label='primary'
          />
          <FormControlLabel
            control={
              <Checkbox 
                checked={checkboxState.checkedB} 
                onChange={handleCheckboxChange}
                name='checkedB'
                color='secondary'
              />
            }
            label='secondary'
          />
        </FormGroup>
        <FormControlLabel
          control={
            <Checkbox 
              icon={<FavoriteBorder />} 
              checkedIcon={<Favorite />} 
              name="checkedH" 
            />
          }
          label="My Favorite"
        />
      </div>
    </main>
  );
}

export default UI_stuff;