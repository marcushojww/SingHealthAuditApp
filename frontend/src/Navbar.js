import React, { useState, useRef, useEffect, useContext } from "react";
import { Link } from "react-router-dom";
import { FaBars } from "react-icons/fa";
import AuthenticationService from "./AuthenticationService";
import { auditorNavLinks, tenantNavLinks, managerNavLinks } from "./data";
import auditor from "./auditor.png";
import { Context } from "./Context";
import { Typography, Button } from "@material-ui/core";
import NotificationsIcon from '@material-ui/icons/Notifications';
import useStyles from "./styles";

function Navbar() {
  const { accountState, getAccountInfo } = useContext(Context);
  const { role_id, username } = accountState;
  const [toggleClicked, settoggleClicked] = useState(false);
  const linksContainerRef = useRef(null);
  const linksRef = useRef(null);
  const styles = useStyles();
  const showLinks = () => {
    settoggleClicked(!toggleClicked);
  };

  useEffect(() => {
    getAccountInfo();
    let linksHeight = linksRef.current.getBoundingClientRect().height;
    if (toggleClicked) {
      linksContainerRef.current.style.height = `${linksHeight}px`;
    } else {
      linksContainerRef.current.style.height = "0px";
    }
  }, [toggleClicked]);

  function mapLinks(links) {
    return links.map((link, index) => {
      const { url, text } = link;
      return (
        <li key={index}>
          <Link to={url}>{text}</Link>
        </li>
      );
    });
  }

  function getNavLinks(role) {
    if (role === "Auditor") {
      return mapLinks(auditorNavLinks);
    } else if (role === "Tenant") {
      return mapLinks(tenantNavLinks);
    } else if (role === "Manager") {
      return mapLinks(managerNavLinks);
    }
  }

  // console.log(role_id);
  // console.log(navLinks);
  // console.log(fakeNavLinks);
  // console.log(navLinks===fakeNavLinks);

  return (
    <nav>
      <div className="nav-pc">
        <div className="nav-mobile">
          <img src={auditor} className="logo" alt="auditor"></img>
          <div className="auditor-name">
            <Typography variant="h6">Welcome {username}</Typography>
          </div>
          <button
            className="nav-toggle"
            onClick={() => {
              showLinks();
            }}
          >
            <FaBars />
          </button>
        </div>
        <div className="links-container" ref={linksContainerRef}>
          <ul className="links" ref={linksRef}>
            {getNavLinks(role_id)}
            <Link
              to={"/"}
              onClick={() => {
                AuthenticationService.logout();
              }}
            >
              Logout
            </Link>
            {(role_id === "Manager") ? null : 
              <Link to="/announcements"><Button><NotificationsIcon className={styles.NotificationsIcon}/></Button></Link>
            }
          </ul>
        </div>
      </div>
    </nav>
  );

  // return (
  //   <nav>
  //     <div className="nav-pc">
  //       <div className="nav-mobile">
  //         <img src={auditor} className="logo" alt="auditor"></img>
  //         <div className="auditor-name">Welcome Marcus {}</div>
  //         <button
  //           className="nav-toggle"
  //           onClick={() => {
  //             showLinks();
  //           }}
  //         >
  //           <FaBars />
  //         </button>
  //       </div>
  //       <div className="links-container" ref={linksContainerRef}>
  //         <ul className="links" ref={linksRef}>
  //           {fakeNavLinks.map((link, index) => {
  //             const { id, url, text } = link;
  //             return (
  //               <li key={index}>
  //                 <Link to={url}>{text}</Link>
  //               </li>
  //             );
  //           })}
  //         </ul>
  //       </div>
  //     </div>
  //   </nav>
  // );
}

export default Navbar;
