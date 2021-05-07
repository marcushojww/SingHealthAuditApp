import { FormGroup } from "@material-ui/core";
import { Typography, Button, Grid, TextField } from "@material-ui/core";
import React, { useState, useEffect, useContext } from "react";
import { useParams } from "react-router-dom";

import { Context } from "../Context";
import Navbar from "../Navbar";
import useStyles from "../styles";
import { getDateString, getTimeString } from "../components/utils";

function Chat() {
  const { chatId } = useParams(); // typeof chatId: String
  const [chatEntriesState, setChatEntriesState] = useState([]);
  const [subjectState, setSubjectState] = useState("");
  const [bodyState, setBodyState] = useState("");
  // Use a state to force
  const {
    allChatsOfUserState,
    getChatEntriesOfUser,
    postChatEntry,
    accountState,
    chatSubmitState,
  } = useContext(Context);
  const styles = useStyles();
  const { acc_id, role_id } = accountState;

  useEffect(() => {
    async function getResponse() {
      try {
        await getChatEntriesOfUser(chatId).then((response) => {
          console.log("Chat: allChatsOfUser: " + response.data);
          setChatEntriesState(response.data.reverse());
          //console.log("state: " + chatEntriesState);
          allChatsOfUserState.map((chat, index) => {});
        });
      } catch {
        console.log("Failed to retrive allChatsOfUser");
      }
    }
    getResponse();
  }, [chatSubmitState]);

  // Check if the message is sent by the user, or received by the user
  function isMine(sender_id) {
    return sender_id === acc_id;
  }

  // Handle the SEND MESSAGE button click
  function handleClick() {
    let parentChatId = chatId;
    let subject = subjectState;
    let messageBody = bodyState;
    let attachments = null; // What is JSON node?
    let date = getDateString(new Date());
    let time = getTimeString();
    console.log("This is calling postNewChatEntry");
    console.log("Date: " + date);
    console.log("Time: " + time);
    console.log("Chat ID: " + parentChatId);
    console.log("Subject: " + subject);
    console.log("Message Body: " + messageBody);
    postChatEntry(parentChatId, subject, messageBody, attachments);
  }

  function subjectChangeHandler(subject_input) {
    setSubjectState(subject_input);
  }

  function bodyChangeHandler(body_input) {
    setBodyState(body_input);
  }

  return (
    <main className={styles.main}>
      <Navbar />
      <br />
      {/* {(role_id === "Auditor") ? 
        <div>
          <Typography variant="h5" align="center">{storeName}</Typography>
          <Typography variant="body2" align="center">ID: {accId}</Typography>
        </div>
        :  */}
      <div>
        <Typography variant="h5" align="center">
          Chat ID: {chatId}
        </Typography>
      </div>
      {/* } */}

      <br />
      <ul className={styles.chat_entries_list}>
        {chatEntriesState.map((entry, index) => {
          return (
            <React.Fragment key={index}>
              <li
                item
                className={
                  isMine(entry.sender_id)
                    ? styles.rightBubble
                    : styles.leftBubble
                }
              >
                <Grid item xs={12} sm container>
                  <Grid item xs container direction="column" spacing={2}>
                    <Grid item xs>
                      <Typography variant="subtitle2" color="textSecondary">
                        Subject: {entry.subject}
                      </Typography>
                      <Typography variant="body1">
                        {entry.messageBody}
                      </Typography>
                    </Grid>
                  </Grid>
                  <Grid item>
                    <Typography variant="body2" color="textSecondary">
                      {entry.date}, {entry.time}
                    </Typography>
                    <Typography variant="body2" color="textSecondary">
                      {entry.attachments}
                    </Typography>
                  </Grid>
                </Grid>
              </li>
            </React.Fragment>
          );
        })}
      </ul>
      <div className={styles.chat_entry_edit}>
        <FormGroup column="true">
          <TextField
            className={styles.message_input}
            label="Subject"
            variant="outlined"
            onChange={(e) => subjectChangeHandler(e.target.value)}
          />
          <TextField
            className={styles.message_input}
            label="Message"
            variant="outlined"
            onChange={(e) => bodyChangeHandler(e.target.value)}
          />
        </FormGroup>

        <Button
          className={styles.big_buttons}
          align="center"
          variant="outlined"
          fullWidth
          color="primary"
          onClick={() => handleClick()}
        >
          Send Message
        </Button>
      </div>
    </main>
  );
}

export default Chat;
