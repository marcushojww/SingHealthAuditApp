import React, { useContext, useEffect, useState } from "react";
import { NavLink } from "react-router-dom";
import { makeStyles } from "@material-ui/core/styles";
import ExpandMoreIcon from "@material-ui/icons/ExpandMore";
import { Button, Grid, FormGroup, Typography } from "@material-ui/core";

import useStyles from "../styles";
import { Context } from "../Context";

const ChatCards = (props) => {
  const styles = useStyles();
  const test = 147;
  const [targetUserState, setTargetUserState] = useState({});
  const { getUserInfo, accountState, chatSubmitState } = useContext(Context);

  // Handle chat target role type (Auditor/Tenant) and target user_id
  var target_role = "Tenant";
  var target_id;
  if (accountState.role_id === "Auditor") {
    target_role = "Tenant";
    target_id = props.chat.tenant_id.toString();
  } else if (accountState.role_id === "Tenant") {
    target_role = "Auditor";
    target_id = props.chat.auditor_id.toString();
  }

  useEffect(() => {
    getUserInfo(target_id)
      .then((response) => {
        setTargetUserState(response.data);
      })
      .catch(() => {
        console.log("Failed to retrieve tenant info");
      });
  }, []);

  return (
    // <NavLink to={{pathname: `/chat/${props.chat.chat_id}/${targetUserState.store_name}/${targetUserState.acc_id}`, test: test}}>
    <NavLink to={{ pathname: `/chat/${props.chat.chat_id}`, test: test }}>
      <Button className={styles.chat_bubble}>
        <Typography variant="subtitle1">{targetUserState.branch_id}</Typography>
        <Typography variant="subtitle1">
          {targetUserState.store_name}
        </Typography>
        <Typography variant="body2" color="textSecondary">
          {target_role} ID: {target_id}
        </Typography>
        <Typography variant="body2" color="textSecondary">
          {targetUserState.username}
        </Typography>
        <Typography variant="body2" color="textSecondary">
          Chat ID: {props.chat.chat_id}
        </Typography>
      </Button>
    </NavLink>
  );
};

export default ChatCards;
