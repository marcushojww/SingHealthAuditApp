import React, { useEffect, useContext, useState } from "react";
import {
  Typography,
  TextField,
  Button,
  FormGroup,
  FormControl,
  InputLabel,
  Select,
} from "@material-ui/core";
import {
  DialogActions,
  DialogContent,
  DialogTitle,
  Dialog,
  DialogContentText,
} from "@material-ui/core";

import { Context } from "../Context";
import Navbar from "../Navbar";
import useStyles from "../styles";
import ChatCards from "../components/ChatCards";

function Chat() {
  const {
    setAllChatsOfUserState,
    allChatsOfUserState,
    getAllChatsOfUser,
    postCreateNewChat,
    accountState,
    chatSubmitState,
    getAllAuditors,
    getAllTenants,
  } = useContext(Context);

  const [allChatsState, setAllChatsState] = useState([]);
  const [auditorIdState, setAuditorIdState] = useState("");
  const [tenantIdState, setTenantIdState] = useState("");
  const [dialogState, setDialogState] = useState(false);
  const [allTenantsState, setAllTenantsState] = useState([]);

  const [newTargetState, setNewTargetState] = useState("Select a tenant");

  const { role_id, acc_id } = accountState;
  const styles = useStyles();
  const chatsArray = [];

  function openDialog() {
    setDialogState(true);
  }
  function closeDialog() {
    setDialogState(false);
  }

  function isAuditor() {
    return role_id === "Auditor";
  }

  function handleNewTargetChange(target_user_string) {
    setNewTargetState(target_user_string);
    var target_id = target_user_string.split("-")[1];
    console.log(target_user_string);
    console.log(target_id);
    console.log(typeof acc_id); // Number
    console.log(typeof target_id); // String
    if (target_id === null) {
      return; // If no target_id is entered: show alert
    }
    if (role_id === "Auditor") {
      setAuditorIdState(acc_id);
      setTenantIdState(target_id);
    } else if (role_id == "Tenant") {
      setTenantIdState(acc_id.toString()); // Number => String
      setAuditorIdState(target_id); // String
    } else {
      console.log("Invalid auditor/tenant ID");
    }
  }

  useEffect(() => {
    getAllTenants()
      .then((response) => {
        setAllTenantsState(response.data);
        console.log("all tenants: " + allTenantsState);
      })
      .catch(() => {
        console.log("Failed to retrieve all tenants");
      });

    async function getResponse() {
      try {
        await getAllChatsOfUser().then((response) => {
          console.log("allChatsOfUser: " + response.data);
          setAllChatsOfUserState(response.data);
        });
      } catch {
        console.log("Failed to retrive allChatsOfUser");
      }
    }
    getResponse();
  }, [chatSubmitState]);

  function handleCreateNewChatClick() {
    console.log("Chat calling postNewChat");
    console.log("auditor id: " + auditorIdState);
    console.log("tenant id: " + tenantIdState);
    // If no tenant is selected
    if (auditorIdState.length === 0 || tenantIdState.length === 0) {
      openDialog();
    } else {
      postCreateNewChat(auditorIdState, tenantIdState);
    }
  }

  return (
    <main className={styles.main}>
      <Navbar />

      {isAuditor() ? (
        <div className={styles.chat_edit}>
          <Typography variant="subtitle1" className={styles.contactlist_title}>
            Select a tenant
          </Typography>
          <FormControl
            variant="outlined"
            className={styles.chats_dialog_selector}
          >
            <Select
              native
              value={newTargetState}
              onChange={(e) => handleNewTargetChange(e.target.value)}
            >
              {allTenantsState.map((tenant, index) => (
                <option
                  key={index}
                  value={`${tenant.store_name}-${tenant.acc_id}`}
                  id={tenant.acc_id}
                >
                  {tenant.branch_id} {tenant.store_name} {tenant.acc_id}
                </option>
              ))}
            </Select>
          </FormControl>

          <Button
            align="center"
            variant="outlined"
            color="secondary"
            className={styles.big_buttons}
            onClick={() => handleCreateNewChatClick()}
          >
            Create Chat
          </Button>
        </div>
      ) : null}

      <br />
      <div className={styles.chat_list}>
        {allChatsOfUserState.map((chat, index) => {
          return (
            <React.Fragment key={index}>
              <ChatCards chat={chat} />
            </React.Fragment>
          );
        })}
      </div>

      <Dialog
        className={styles.post_new_announcement_dialog}
        open={dialogState}
        onClose={closeDialog}
        aria-labelledby="alert-dialog-title"
        aria-describedby="alert-dialog-description"
      >
        <DialogTitle id="alert-dialog-title">
          {"Failed to create new chat"}
        </DialogTitle>
        <DialogContent>
          <DialogContentText id="alert-dialog-description">
            Please enter a valid user ID
          </DialogContentText>
        </DialogContent>
        <DialogActions>
          <Button onClick={closeDialog} color="primary">
            Ok
          </Button>
        </DialogActions>
      </Dialog>
    </main>
  );
}

export default Chat;
