import { NgModule }                     from '@angular/core';
import { BrowserModule }                from '@angular/platform-browser';
import { HttpModule }                   from '@angular/http';

import { AppComponent }                 from './app.component';
import { Ng2BootstrapModule }           from 'ng2-bootstrap/ng2-bootstrap';
import { NAV_DROPDOWN_DIRECTIVES }      from './shared/nav-dropdown.directive';

import { DataTableModule, SharedModule } from 'primeng/primeng';

//import { ChartsModule }                 from 'ng2-charts/ng2-charts';
import { SIDEBAR_TOGGLE_DIRECTIVES }    from './shared/sidebar.directive';
import { AsideToggleDirective }         from './shared/aside.directive';
import { BreadcrumbsComponent }         from './shared/breadcrumb.component';
import { routing }                      from './app.routing';

//Layouts
import { FullLayoutComponent }          from './layouts/full-layout.component';
import { SimpleLayoutComponent }        from './layouts/simple-layout.component';

//Main view
import { DashboardComponent }           from './dashboard/dashboard.component';

//Data Management
import { DataManagementComponent }      from './data_management/data-management.component';

//Survey Designer
import { SurveyDesignerHomeComponent }  from './survey_designer/survey-designer-home.component';
import { SurveyTableComponent }         from './survey_designer/survey-table.component';

//import { CardsComponent }               from './components/cards.component';
//import { FormsComponent }               from './components/forms.component';
//import { SocialButtonsComponent }       from './components/social-buttons.component';
//import { SwitchesComponent }            from './components/switches.component';
//import { TablesComponent }              from './components/tables.component';

//Icons
import { FontAwesomeComponent }         from './icons/font-awesome.component';
import { SimpleLineIconsComponent }     from './icons/simple-line-icons.component';

//Widgets
//import { WidgetsComponent }             from './widgets/widgets.component';

//Charts
//import { ChartsComponent }              from './charts/charts.component';

//Pages
//import { p404Component }                from './pages/404.component';
//import { p500Component }                from './pages/500.component';
//import { LoginComponent }               from './pages/login.component';
//import { RegisterComponent }            from './pages/register.component';

@NgModule({
  imports: [
    BrowserModule,
    routing,
    Ng2BootstrapModule,
    HttpModule,
    
    DataTableModule,
    SharedModule
//    ChartsModule
  ],
  declarations: [
    AppComponent,
    FullLayoutComponent,
    SimpleLayoutComponent,
    DashboardComponent,
    DataManagementComponent,
    SurveyDesignerHomeComponent,
    SurveyTableComponent,
//    CardsComponent,
//    FormsComponent,
//    SocialButtonsComponent,
//    SwitchesComponent,
//    TablesComponent,
//    FontAwesomeComponent,
//    SimpleLineIconsComponent,
//    WidgetsComponent,
//    ChartsComponent,
//    p404Component,
//    p500Component,
//    LoginComponent,
//    RegisterComponent,
    NAV_DROPDOWN_DIRECTIVES,
    BreadcrumbsComponent,
    SIDEBAR_TOGGLE_DIRECTIVES,
    AsideToggleDirective,
  ],
  bootstrap: [ AppComponent ]
})
export class AppModule { }