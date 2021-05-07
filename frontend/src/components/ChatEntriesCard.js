import React, { useContext, useEffect, useState } from "react";
import { Context } from "../Context";
import Accordion from "@material-ui/core/Accordion";
import AccordionSummary from "@material-ui/core/AccordionSummary";
import AccordionDetails from "@material-ui/core/AccordionDetails";
import Typography from "@material-ui/core/Typography";
import ExpandMoreIcon from "@material-ui/icons/ExpandMore";
import AccordionActions from "@material-ui/core/AccordionActions";
import Chip from "@material-ui/core/Chip";
import Button from "@material-ui/core/Button";
import Divider from "@material-ui/core/Divider";
import Chat from "../pages/Chat";

const ChatEntriesCards = () => {
  const { chatEntriesOfUserState, setChatEntriesOfUserState } = useContext(
    Context
  );
  console.log(chatEntriesOfUserState);
  // console.log("chatEntries: " + chatEntriesOfUserState);
  // console.log("dict: " + chatEntriesOfUserState[2]);
  return (
    <div>
      {
        chatEntriesOfUserState &&
          chatEntriesOfUserState.map((entry, index) => {
            return (
              <React.Fragment key={index}>
                <AccordionDetails>
                  <Typography>
                    <div>Chat Entry ID: {entry.chat_entry_id}</div>
                    <div>Subject:{entry.subject}</div>
                    <div>Sender ID: {entry.sender_id}</div>
                    <div>Time: {entry.time}</div>
                    <div>Date: {entry.date}</div>
                    <div>Message Body: {entry.messageBody}</div>
                    <div>Attachments: {entry.attachments}</div>
                    <br />
                  </Typography>
                </AccordionDetails>
                <AccordionActions>
                  <Button size="small" color="primary">
                    Reply
                  </Button>
                </AccordionActions>
                <Divider />
              </React.Fragment>
            );
          })

        // for (var key in Object.keys(chatEntriesOfUserState)) {
        //   if (chatEntriesOfUserState[key].length === 0) {
        //     return (
        //       <React.Fragment>
        //         <AccordionDetails>
        //           <Typography>
        //             No message.
        //           </Typography>
        //         </AccordionDetails>
        //       </React.Fragment>
        //     )
        //   } else {
        //     chatEntriesOfUserState.map((entry, index) => {
        //     //console.log("chatEntry1: " + chatEntriesOfUserState[0]);
        //       return (
        //         <React.Fragment key={index}>
        //           <AccordionDetails>
        //             <Typography>
        //               <div>Chat Entry ID: {entry.chat_entry_id}</div>
        //               <div>Date: {entry.date}</div>
        //               <div>Message Body: {entry.messageBody}</div>
        //               <div>Sender ID: {entry.sender_id}</div>
        //               <div>Subject:{entry.subject}</div>
        //               <div>Time: {entry.time}</div>
        //               <div>Attachments: {entry.attachments}</div>
        //               <br />
        //             </Typography>
        //           </AccordionDetails>
        //         </React.Fragment>
        //       );}
        //     )
        //   }
        // }
      }
    </div>
  );
};

export default ChatEntriesCards;
