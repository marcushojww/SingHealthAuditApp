//import logo from "./logo.svg";
import React from "react";
import {
  BrowserRouter as Router,
  Route,
  Switch,
  Redirect,
} from "react-router-dom";

import Account from "./pages/Account";
import Tenant from "./pages/Tenant";
import Error from "./pages/Error";
import Institutions from "./pages/Institutions";
import Institution from "./pages/Institution";
import FbChecklist from "./pages/FbChecklist";
import NonFbChecklist from "./pages/NonFbChecklist";
import SMAChecklist from "./pages/SMAChecklist";
import AuthenticatedRoute from "./components/testJwt/AuthenticatedRoute";
import LoginComponent from "./components/testJwt/Login";
import HomeAuditor from "./pages/HomeAuditor";
import Loading from "./pages/Loading";
import AuditReport from "./pages/AuditReport";
import { ContextProvider } from "./Context";
import TenantHome from "./pages/TenantHome";
import ManagerHome from "./pages/ManagerHome";
import Store from "./pages/Store";
import EditAccount from "./pages/EditAccount";
import EditPassword from "./pages/EditPassword";
import AllChats from "./pages/AllChats";
import Chat from "./pages/Chat";
import TenantReport from "./pages/TenantReport";
import AuditEmail from "./pages/AuditEmail";
import Announcement from "./pages/Announcements";
import StoreReport from "./pages/StoreReport";

function App() {
  return (
    <Router>
      <ContextProvider>
        <Switch>
          <Route exact path="/" component={LoginComponent} />
          <AuthenticatedRoute exact path="/home/a" component={HomeAuditor} />
          <AuthenticatedRoute exact path="/home/t" component={TenantHome} />
          <AuthenticatedRoute exact path="/home/m" component={ManagerHome} />
          <AuthenticatedRoute exact path="/account" component={Account} />
          <AuthenticatedRoute
            exact
            path="/edit_account"
            component={EditAccount}
          />
          <AuthenticatedRoute
            exact
            path="/edit_password"
            component={EditPassword}
          />
          <AuthenticatedRoute
            exact
            path="/institutions"
            component={Institutions}
          />
          <AuthenticatedRoute
            exact
            path="/institution/:institutionName"
            component={Institution}
          />
          <AuthenticatedRoute
            exact
            path="/tenant/:tenantId"
            component={Tenant}
          />
          <AuthenticatedRoute
            exact
            path="/tenant/report/:reportId"
            component={AuditReport}
          />
          <AuthenticatedRoute
            exact
            path="/tenant/fbChecklist/:tenantId"
            component={FbChecklist}
          />
          <AuthenticatedRoute
            exact
            path="/tenant/nfbChecklist/:tenantId"
            component={NonFbChecklist}
          />
          <AuthenticatedRoute
            exact
            path="/tenant/smaChecklist/:tenantId"
            component={SMAChecklist}
          />
          <AuthenticatedRoute
            exact
            path="/tenant/report/:reportId"
            component={AuditReport}
          />
          <AuthenticatedRoute
            exact
            path="/tenant/email/:reportId"
            component={AuditEmail}
          />
          <AuthenticatedRoute
            exact
            path="/t/report/:reportId"
            component={TenantReport}
          />
          <AuthenticatedRoute exact path="/store" component={Store} />

          <AuthenticatedRoute exact path="/allChats" component={AllChats} />

          <AuthenticatedRoute
            exact
            path="/fullreport/:reportId"
            component={StoreReport}
          />
          <AuthenticatedRoute exact path="/chat/:chatId" component={Chat} />
          <AuthenticatedRoute
            exact
            path="/announcements"
            component={Announcement}
          />

          <AuthenticatedRoute exact path="/error" component={Error} />
          <Redirect to="/" component={LoginComponent} />
        </Switch>
      </ContextProvider>
    </Router>
  );
}

export default App;
