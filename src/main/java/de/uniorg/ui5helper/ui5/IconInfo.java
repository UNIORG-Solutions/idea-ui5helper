package de.uniorg.ui5helper.ui5;

import java.util.Optional;

public class IconInfo {

    // yes, i am ashamed of this code.
    public static Optional<Character> getIconCode(String name) {
        switch (name) {
            case "accidental-leave":
                return Optional.of((char) 0xe000);
            case "account":
                return Optional.of((char) 0xe001);
            case "wrench":
                return Optional.of((char) 0xe002);
            case "windows-doors":
                return Optional.of((char) 0xe003);
            case "washing-machine":
                return Optional.of((char) 0xe004);
            case "visits":
                return Optional.of((char) 0xe005);
            case "video":
                return Optional.of((char) 0xe006);
            case "travel-expense":
                return Optional.of((char) 0xe007);
            case "temperature":
                return Optional.of((char) 0xe008);
            case "task":
                return Optional.of((char) 0xe009);
            case "synchronize":
                return Optional.of((char) 0xe00a);
            case "survey":
                return Optional.of((char) 0xe00b);
            case "settings":
                return Optional.of((char) 0xe00c);
            case "search":
                return Optional.of((char) 0xe00d);
            case "sales-document":
                return Optional.of((char) 0xe00e);
            case "retail-store":
                return Optional.of((char) 0xe00f);
            case "refresh":
                return Optional.of((char) 0xe010);
            case "product":
                return Optional.of((char) 0xe011);
            case "present":
                return Optional.of((char) 0xe012);
            case "ppt-attachment":
                return Optional.of((char) 0xe013);
            case "pool":
                return Optional.of((char) 0xe014);
            case "pie-chart":
                return Optional.of((char) 0xe015);
            case "picture":
                return Optional.of((char) 0xe016);
            case "photo-voltaic":
                return Optional.of((char) 0xe017);
            case "phone":
                return Optional.of((char) 0xe018);
            case "pending":
                return Optional.of((char) 0xe019);
            case "pdf-attachment":
                return Optional.of((char) 0xe01a);
            case "past":
                return Optional.of((char) 0xe01b);
            case "outgoing-call":
                return Optional.of((char) 0xe01c);
            case "opportunity":
                return Optional.of((char) 0xe01d);
            case "opportunities":
                return Optional.of((char) 0xe01e);
            case "notes":
                return Optional.of((char) 0xe01f);
            case "money-bills":
                return Optional.of((char) 0xe020);
            case "map":
                return Optional.of((char) 0xe021);
            case "log":
                return Optional.of((char) 0xe022);
            case "line-charts":
                return Optional.of((char) 0xe023);
            case "lightbulb":
                return Optional.of((char) 0xe024);
            case "leads":
                return Optional.of((char) 0xe025);
            case "lead":
                return Optional.of((char) 0xe026);
            case "laptop":
                return Optional.of((char) 0xe027);
            case "kpi-managing-my-area":
                return Optional.of((char) 0xe028);
            case "kpi-corporate-performance":
                return Optional.of((char) 0xe029);
            case "incoming-call":
                return Optional.of((char) 0xe02a);
            case "inbox":
                return Optional.of((char) 0xe02b);
            case "horizontal-bar-chart":
                return Optional.of((char) 0xe02c);
            case "history":
                return Optional.of((char) 0xe02d);
            case "heating-cooling":
                return Optional.of((char) 0xe02e);
            case "gantt-bars":
                return Optional.of((char) 0xe02f);
            case "future":
                return Optional.of((char) 0xe030);
            case "fridge":
                return Optional.of((char) 0xe031);
            case "fallback":
                return Optional.of((char) 0xe032);
            case "expense-report":
                return Optional.of((char) 0xe033);
            case "excel-attachment":
                return Optional.of((char) 0xe034);
            case "energy-saving-lightbulb":
                return Optional.of((char) 0xe035);
            case "employee":
                return Optional.of((char) 0xe036);
            case "email":
                return Optional.of((char) 0xe037);
            case "edit":
                return Optional.of((char) 0xe038);
            case "duplicate":
                return Optional.of((char) 0xe039);
            case "download":
                return Optional.of((char) 0xe03a);
            case "doc-attachment":
                return Optional.of((char) 0xe03b);
            case "dishwasher":
                return Optional.of((char) 0xe03c);
            case "delete":
                return Optional.of((char) 0xe03d);
            case "decline":
                return Optional.of((char) 0xe03e);
            case "complete":
                return Optional.of((char) 0xe03f);
            case "competitor":
                return Optional.of((char) 0xe040);
            case "collections-management":
                return Optional.of((char) 0xe041);
            case "chalkboard":
                return Optional.of((char) 0xe042);
            case "cart":
                return Optional.of((char) 0xe043);
            case "card":
                return Optional.of((char) 0xe044);
            case "camera":
                return Optional.of((char) 0xe045);
            case "calendar":
                return Optional.of((char) 0xe046);
            case "begin":
                return Optional.of((char) 0xe047);
            case "basket":
                return Optional.of((char) 0xe048);
            case "bar-chart":
                return Optional.of((char) 0xe049);
            case "attachment":
                return Optional.of((char) 0xe04a);
            case "arrow-top":
                return Optional.of((char) 0xe04b);
            case "arrow-right":
                return Optional.of((char) 0xe04c);
            case "arrow-left":
                return Optional.of((char) 0xe04d);
            case "arrow-bottom":
                return Optional.of((char) 0xe04e);
            case "approvals":
                return Optional.of((char) 0xe04f);
            case "appointment":
                return Optional.of((char) 0xe050);
            case "alphabetical-order":
                return Optional.of((char) 0xe051);
            case "along-stacked-chart":
                return Optional.of((char) 0xe052);
            case "alert":
                return Optional.of((char) 0xe053);
            case "addresses":
                return Optional.of((char) 0xe054);
            case "address-book":
                return Optional.of((char) 0xe055);
            case "add-filter":
                return Optional.of((char) 0xe056);
            case "add-favorite":
                return Optional.of((char) 0xe057);
            case "add":
                return Optional.of((char) 0xe058);
            case "activities":
                return Optional.of((char) 0xe059);
            case "action":
                return Optional.of((char) 0xe05a);
            case "accept":
                return Optional.of((char) 0xe05b);
            case "hint":
                return Optional.of((char) 0xe05c);
            case "group":
                return Optional.of((char) 0xe05d);
            case "check-availability":
                return Optional.of((char) 0xe05e);
            case "weather-proofing":
                return Optional.of((char) 0xe05f);
            case "payment-approval":
                return Optional.of((char) 0xe060);
            case "batch-payments":
                return Optional.of((char) 0xe061);
            case "bed":
                return Optional.of((char) 0xe062);
            case "arobase":
                return Optional.of((char) 0xe063);
            case "family-care":
                return Optional.of((char) 0xe064);
            case "favorite":
                return Optional.of((char) 0xe065);
            case "navigation-right-arrow":
                return Optional.of((char) 0xe066);
            case "navigation-left-arrow":
                return Optional.of((char) 0xe067);
            case "e-care":
                return Optional.of((char) 0xe068);
            case "less":
                return Optional.of((char) 0xe069);
            case "lateness":
                return Optional.of((char) 0xe06a);
            case "lab":
                return Optional.of((char) 0xe06b);
            case "internet-browser":
                return Optional.of((char) 0xe06c);
            case "instance":
                return Optional.of((char) 0xe06d);
            case "inspection":
                return Optional.of((char) 0xe06e);
            case "image-viewer":
                return Optional.of((char) 0xe06f);
            case "home":
                return Optional.of((char) 0xe070);
            case "grid":
                return Optional.of((char) 0xe071);
            case "goalseek":
                return Optional.of((char) 0xe072);
            case "general-leave-request":
                return Optional.of((char) 0xe073);
            case "create-leave-request":
                return Optional.of((char) 0xe074);
            case "flight":
                return Optional.of((char) 0xe075);
            case "filter":
                return Optional.of((char) 0xe076);
            case "favorite-list":
                return Optional.of((char) 0xe077);
            case "factory":
                return Optional.of((char) 0xe078);
            case "endoscopy":
                return Optional.of((char) 0xe079);
            case "employee-pane":
                return Optional.of((char) 0xe07a);
            case "employee-approvals":
                return Optional.of((char) 0xe07b);
            case "email-read":
                return Optional.of((char) 0xe07c);
            case "electrocardiogram":
                return Optional.of((char) 0xe07d);
            case "documents":
                return Optional.of((char) 0xe07e);
            case "decision":
                return Optional.of((char) 0xe07f);
            case "database":
                return Optional.of((char) 0xe080);
            case "customer-history":
                return Optional.of((char) 0xe081);
            case "customer":
                return Optional.of((char) 0xe082);
            case "credit-card":
                return Optional.of((char) 0xe083);
            case "create-entry-time":
                return Optional.of((char) 0xe084);
            case "contacts":
                return Optional.of((char) 0xe085);
            case "compare":
                return Optional.of((char) 0xe086);
            case "clinical-order":
                return Optional.of((char) 0xe087);
            case "chain-link":
                return Optional.of((char) 0xe088);
            case "pull-down":
                return Optional.of((char) 0xe089);
            case "cargo-train":
                return Optional.of((char) 0xe08a);
            case "car-rental":
                return Optional.of((char) 0xe08b);
            case "business-card":
                return Optional.of((char) 0xe08c);
            case "bar-code":
                return Optional.of((char) 0xe08d);
            case "folder-blank":
                return Optional.of((char) 0xe08e);
            case "passenger-train":
                return Optional.of((char) 0xe08f);
            case "question-mark":
                return Optional.of((char) 0xe090);
            case "world":
                return Optional.of((char) 0xe091);
            case "iphone":
                return Optional.of((char) 0xe092);
            case "ipad":
                return Optional.of((char) 0xe093);
            case "warning":
                return Optional.of((char) 0xe094);
            case "sort":
                return Optional.of((char) 0xe095);
            case "course-book":
                return Optional.of((char) 0xe096);
            case "course-program":
                return Optional.of((char) 0xe097);
            case "add-coursebook":
                return Optional.of((char) 0xe098);
            case "print":
                return Optional.of((char) 0xe099);
            case "save":
                return Optional.of((char) 0xe09a);
            case "play":
                return Optional.of((char) 0xe09b);
            case "pause":
                return Optional.of((char) 0xe09c);
            case "record":
                return Optional.of((char) 0xe09d);
            case "response":
                return Optional.of((char) 0xe09e);
            case "pushpin-on":
                return Optional.of((char) 0xe09f);
            case "pushpin-off":
                return Optional.of((char) 0xe0a0);
            case "unfavorite":
                return Optional.of((char) 0xe0a1);
            case "learning-assistant":
                return Optional.of((char) 0xe0a2);
            case "timesheet":
                return Optional.of((char) 0xe0a3);
            case "time-entry-request":
                return Optional.of((char) 0xe0a4);
            case "list":
                return Optional.of((char) 0xe0a5);
            case "action-settings":
                return Optional.of((char) 0xe0a6);
            case "share":
                return Optional.of((char) 0xe0a7);
            case "feed":
                return Optional.of((char) 0xe0a8);
            case "role":
                return Optional.of((char) 0xe0a9);
            case "flag":
                return Optional.of((char) 0xe0aa);
            case "post":
                return Optional.of((char) 0xe0ab);
            case "inspect":
                return Optional.of((char) 0xe0ac);
            case "inspect-down":
                return Optional.of((char) 0xe0ad);
            case "appointment-2":
                return Optional.of((char) 0xe0ae);
            case "target-group":
                return Optional.of((char) 0xe0af);
            case "marketing-campaign":
                return Optional.of((char) 0xe0b0);
            case "notification":
                return Optional.of((char) 0xe0b1);
            case "message-error":
                return Optional.of((char) 0xe0b1);
            case "comment":
                return Optional.of((char) 0xe0b2);
            case "shipping-status":
                return Optional.of((char) 0xe0b3);
            case "collaborate":
                return Optional.of((char) 0xe0b4);
            case "shortcut":
                return Optional.of((char) 0xe0b5);
            case "lead-outdated":
                return Optional.of((char) 0xe0b6);
            case "tools-opportunity":
                return Optional.of((char) 0xe0b7);
            case "permission":
                return Optional.of((char) 0xe0b8);
            case "supplier":
                return Optional.of((char) 0xe0b9);
            case "table-view":
                return Optional.of((char) 0xe0ba);
            case "table-chart":
                return Optional.of((char) 0xe0bb);
            case "switch-views":
                return Optional.of((char) 0xe0bc);
            case "e-learning":
                return Optional.of((char) 0xe0bd);
            case "manager":
                return Optional.of((char) 0xe0be);
            case "switch-classes":
                return Optional.of((char) 0xe0bf);
            case "simple-payment":
                return Optional.of((char) 0xe0c0);
            case "signature":
                return Optional.of((char) 0xe0c1);
            case "sales-order-item":
                return Optional.of((char) 0xe0c2);
            case "sales-order":
                return Optional.of((char) 0xe0c3);
            case "request":
                return Optional.of((char) 0xe0c4);
            case "receipt":
                return Optional.of((char) 0xe0c5);
            case "puzzle":
                return Optional.of((char) 0xe0c6);
            case "process":
                return Optional.of((char) 0xe0c7);
            case "private":
                return Optional.of((char) 0xe0c8);
            case "popup-window":
                return Optional.of((char) 0xe0c9);
            case "person-placeholder":
                return Optional.of((char) 0xe0ca);
            case "per-diem":
                return Optional.of((char) 0xe0cb);
            case "paper-plane":
                return Optional.of((char) 0xe0cc);
            case "paid-leave":
                return Optional.of((char) 0xe0cd);
            case "pdf-reader":
                return Optional.of((char) 0xe0ce);
            case "overview-chart":
                return Optional.of((char) 0xe0cf);
            case "overlay":
                return Optional.of((char) 0xe0d0);
            case "org-chart":
                return Optional.of((char) 0xe0d1);
            case "number-sign":
                return Optional.of((char) 0xe0d2);
            case "notification-2":
                return Optional.of((char) 0xe0d3);
            case "my-sales-order":
                return Optional.of((char) 0xe0d4);
            case "meal":
                return Optional.of((char) 0xe0d5);
            case "loan":
                return Optional.of((char) 0xe0d6);
            case "order-status":
                return Optional.of((char) 0xe0d7);
            case "customer-order-entry":
                return Optional.of((char) 0xe0d8);
            case "performance":
                return Optional.of((char) 0xe0d9);
            case "menu":
                return Optional.of((char) 0xe0da);
            case "employee-lookup":
                return Optional.of((char) 0xe0db);
            case "education":
                return Optional.of((char) 0xe0dc);
            case "customer-briefing":
                return Optional.of((char) 0xe0dd);
            case "customer-and-contacts":
                return Optional.of((char) 0xe0de);
            case "my-view":
                return Optional.of((char) 0xe0df);
            case "accelerated":
                return Optional.of((char) 0xe0e0);
            case "to-be-reviewed":
                return Optional.of((char) 0xe0e1);
            case "warning2":
                return Optional.of((char) 0xe0e2);
            case "feeder-arrow":
                return Optional.of((char) 0xe0e3);
            case "quality-issue":
                return Optional.of((char) 0xe0e4);
            case "workflow-tasks":
                return Optional.of((char) 0xe0e5);
            case "create":
                return Optional.of((char) 0xe0e6);
            case "home-share":
                return Optional.of((char) 0xe0e7);
            case "globe":
                return Optional.of((char) 0xe0e8);
            case "tags":
                return Optional.of((char) 0xe0e9);
            case "work-history":
                return Optional.of((char) 0xe0ea);
            case "x-ray":
                return Optional.of((char) 0xe0eb);
            case "wounds-doc":
                return Optional.of((char) 0xe0ec);
            case "web-cam":
                return Optional.of((char) 0xe0ed);
            case "waiver":
                return Optional.of((char) 0xe0ee);
            case "vertical-bar-chart":
                return Optional.of((char) 0xe0ef);
            case "upstacked-chart":
                return Optional.of((char) 0xe0f0);
            case "trip-report":
                return Optional.of((char) 0xe0f1);
            case "microphone":
                return Optional.of((char) 0xe0f2);
            case "unpaid-leave":
                return Optional.of((char) 0xe0f3);
            case "tree":
                return Optional.of((char) 0xe0f4);
            case "toaster-up":
                return Optional.of((char) 0xe0f5);
            case "toaster-top":
                return Optional.of((char) 0xe0f6);
            case "toaster-down":
                return Optional.of((char) 0xe0f7);
            case "time-account":
                return Optional.of((char) 0xe0f8);
            case "theater":
                return Optional.of((char) 0xe0f9);
            case "taxi":
                return Optional.of((char) 0xe0fa);
            case "subway-train":
                return Optional.of((char) 0xe0fb);
            case "study-leave":
                return Optional.of((char) 0xe0fc);
            case "stethoscope":
                return Optional.of((char) 0xe0fd);
            case "step":
                return Optional.of((char) 0xe0fe);
            case "sonography":
                return Optional.of((char) 0xe0ff);
            case "soccor":
                return Optional.of((char) 0xe100);
            case "physical-activity":
                return Optional.of((char) 0xe101);
            case "pharmacy":
                return Optional.of((char) 0xe102);
            case "official-service":
                return Optional.of((char) 0xe103);
            case "offsite-work":
                return Optional.of((char) 0xe104);
            case "nutrition-activity":
                return Optional.of((char) 0xe105);
            case "newspaper":
                return Optional.of((char) 0xe106);
            case "monitor-payments":
                return Optional.of((char) 0xe107);
            case "map-2":
                return Optional.of((char) 0xe108);
            case "machine":
                return Optional.of((char) 0xe109);
            case "mri-scan":
                return Optional.of((char) 0xe10a);
            case "end-user-experience-monitoring":
                return Optional.of((char) 0xe10b);
            case "unwired":
                return Optional.of((char) 0xe10c);
            case "customer-financial-fact-sheet":
                return Optional.of((char) 0xe10d);
            case "retail-store-manager":
                return Optional.of((char) 0xe10e);
            case "Netweaver-business-client":
                return Optional.of((char) 0xe10f);
            case "electronic-medical-record":
                return Optional.of((char) 0xe110);
            case "eam-work-order":
                return Optional.of((char) 0xe111);
            case "customer-view":
                return Optional.of((char) 0xe112);
            case "crm-service-manager":
                return Optional.of((char) 0xe113);
            case "crm-sales":
                return Optional.of((char) 0xe114);
            case "widgets":
                return Optional.of((char) 0xe115);
            case "commission-check":
                return Optional.of((char) 0xe116);
            case "collections-insight":
                return Optional.of((char) 0xe117);
            case "clinical-tast-tracker":
                return Optional.of((char) 0xe118);
            case "citizen-connect":
                return Optional.of((char) 0xe119);
            case "cart-approval":
                return Optional.of((char) 0xe11a);
            case "capital-projects":
                return Optional.of((char) 0xe11b);
            case "bo-strategy-management":
                return Optional.of((char) 0xe11c);
            case "business-objects-mobile":
                return Optional.of((char) 0xe11d);
            case "business-objects-explorer":
                return Optional.of((char) 0xe11e);
            case "business-objects-experience":
                return Optional.of((char) 0xe11f);
            case "bbyd-dashboard":
                return Optional.of((char) 0xe120);
            case "bbyd-active-sales":
                return Optional.of((char) 0xe121);
            case "business-by-design":
                return Optional.of((char) 0xe122);
            case "business-one":
                return Optional.of((char) 0xe123);
            case "sap-box":
                return Optional.of((char) 0xe124);
            case "manager-insight":
                return Optional.of((char) 0xe125);
            case "accounting-document-verification":
                return Optional.of((char) 0xe126);
            case "hr-approval":
                return Optional.of((char) 0xe127);
            case "idea-wall":
                return Optional.of((char) 0xe128);
            case "Chart-Tree-Map":
                return Optional.of((char) 0xe129);
            case "cart-5":
                return Optional.of((char) 0xe12a);
            case "cart-4":
                return Optional.of((char) 0xe12b);
            case "wallet":
                return Optional.of((char) 0xe12c);
            case "vehicle-repair":
                return Optional.of((char) 0xe12d);
            case "upload":
                return Optional.of((char) 0xe12e);
            case "unlocked":
                return Optional.of((char) 0xe12f);
            case "umbrella":
                return Optional.of((char) 0xe130);
            case "travel-request":
                return Optional.of((char) 0xe131);
            case "travel-expense-report":
                return Optional.of((char) 0xe132);
            case "travel-itinerary":
                return Optional.of((char) 0xe133);
            case "time-overtime":
                return Optional.of((char) 0xe134);
            case "thing-type":
                return Optional.of((char) 0xe135);
            case "technical-object":
                return Optional.of((char) 0xe136);
            case "tag":
                return Optional.of((char) 0xe137);
            case "syringe":
                return Optional.of((char) 0xe138);
            case "syntax":
                return Optional.of((char) 0xe139);
            case "suitcase":
                return Optional.of((char) 0xe13a);
            case "simulate":
                return Optional.of((char) 0xe13b);
            case "shield":
                return Optional.of((char) 0xe13c);
            case "share-2":
                return Optional.of((char) 0xe13d);
            case "sales-quote":
                return Optional.of((char) 0xe13e);
            case "repost":
                return Optional.of((char) 0xe13f);
            case "provision":
                return Optional.of((char) 0xe140);
            case "projector":
                return Optional.of((char) 0xe141);
            case "add-product":
                return Optional.of((char) 0xe142);
            case "pipeline-analysis":
                return Optional.of((char) 0xe143);
            case "add-photo":
                return Optional.of((char) 0xe144);
            case "palette":
                return Optional.of((char) 0xe145);
            case "nurse":
                return Optional.of((char) 0xe146);
            case "sales-notification":
                return Optional.of((char) 0xe147);
            case "mileage":
                return Optional.of((char) 0xe148);
            case "meeting-room":
                return Optional.of((char) 0xe149);
            case "media-forward":
                return Optional.of((char) 0xe14a);
            case "media-play":
                return Optional.of((char) 0xe14b);
            case "media-pause":
                return Optional.of((char) 0xe14c);
            case "media-reverse":
                return Optional.of((char) 0xe14d);
            case "media-rewind":
                return Optional.of((char) 0xe14e);
            case "measurement-document":
                return Optional.of((char) 0xe14f);
            case "measuring-point":
                return Optional.of((char) 0xe150);
            case "measure":
                return Optional.of((char) 0xe151);
            case "map-3":
                return Optional.of((char) 0xe152);
            case "locked":
                return Optional.of((char) 0xe153);
            case "letter":
                return Optional.of((char) 0xe154);
            case "journey-arrive":
                return Optional.of((char) 0xe155);
            case "journey-change":
                return Optional.of((char) 0xe156);
            case "journey-depart":
                return Optional.of((char) 0xe157);
            case "it-system":
                return Optional.of((char) 0xe158);
            case "it-instance":
                return Optional.of((char) 0xe159);
            case "it-host":
                return Optional.of((char) 0xe15a);
            case "iphone-2":
                return Optional.of((char) 0xe15b);
            case "ipad-2":
                return Optional.of((char) 0xe15c);
            case "inventory":
                return Optional.of((char) 0xe15d);
            case "insurance-house":
                return Optional.of((char) 0xe15e);
            case "insurance-life":
                return Optional.of((char) 0xe15f);
            case "insurance-car":
                return Optional.of((char) 0xe160);
            case "initiative":
                return Optional.of((char) 0xe161);
            case "incident":
                return Optional.of((char) 0xe162);
            case "group-2":
                return Optional.of((char) 0xe163);
            case "goal":
                return Optional.of((char) 0xe164);
            case "functional-location":
                return Optional.of((char) 0xe165);
            case "full-screen":
                return Optional.of((char) 0xe166);
            case "form":
                return Optional.of((char) 0xe167);
            case "fob-watch":
                return Optional.of((char) 0xe168);
            case "blank-tag":
                return Optional.of((char) 0xe169);
            case "family-protection":
                return Optional.of((char) 0xe16a);
            case "folder":
                return Optional.of((char) 0xe16b);
            case "fax-machine":
                return Optional.of((char) 0xe16c);
            case "example":
                return Optional.of((char) 0xe16d);
            case "eraser":
                return Optional.of((char) 0xe16e);
            case "employee-rejections":
                return Optional.of((char) 0xe16f);
            case "drop-down-list":
                return Optional.of((char) 0xe170);
            case "draw-rectangle":
                return Optional.of((char) 0xe171);
            case "document":
                return Optional.of((char) 0xe172);
            case "doctor":
                return Optional.of((char) 0xe173);
            case "discussion-2":
                return Optional.of((char) 0xe174);
            case "discussion":
                return Optional.of((char) 0xe175);
            case "dimension":
                return Optional.of((char) 0xe176);
            case "customer-and-supplier":
                return Optional.of((char) 0xe177);
            case "crop":
                return Optional.of((char) 0xe178);
            case "add-contact":
                return Optional.of((char) 0xe179);
            case "compare-2":
                return Optional.of((char) 0xe17a);
            case "color-fill":
                return Optional.of((char) 0xe17b);
            case "collision":
                return Optional.of((char) 0xe17c);
            case "curriculum":
                return Optional.of((char) 0xe17d);
            case "chart-axis":
                return Optional.of((char) 0xe17e);
            case "full-stacked-chart":
                return Optional.of((char) 0xe17f);
            case "full-stacked-column-chart":
                return Optional.of((char) 0xe180);
            case "vertical-bar-chart-2":
                return Optional.of((char) 0xe181);
            case "horizontal-bar-chart-2":
                return Optional.of((char) 0xe182);
            case "horizontal-stacked-chart":
                return Optional.of((char) 0xe183);
            case "vertical-stacked-chart":
                return Optional.of((char) 0xe184);
            case "choropleth-chart":
                return Optional.of((char) 0xe185);
            case "geographic-bubble-chart":
                return Optional.of((char) 0xe186);
            case "multiple-radar-chart":
                return Optional.of((char) 0xe187);
            case "radar-chart":
                return Optional.of((char) 0xe188);
            case "crossed-line-chart":
                return Optional.of((char) 0xe189);
            case "multiple-line-chart":
                return Optional.of((char) 0xe18a);
            case "multiple-bar-chart":
                return Optional.of((char) 0xe18b);
            case "line-chart":
                return Optional.of((char) 0xe18c);
            case "line-chart-dual-axis":
                return Optional.of((char) 0xe18d);
            case "bubble-chart":
                return Optional.of((char) 0xe18e);
            case "scatter-chart":
                return Optional.of((char) 0xe18f);
            case "multiple-pie-chart":
                return Optional.of((char) 0xe190);
            case "column-chart-dual-axis":
                return Optional.of((char) 0xe191);
            case "tag-cloud-chart":
                return Optional.of((char) 0xe192);
            case "area-chart":
                return Optional.of((char) 0xe193);
            case "cause":
                return Optional.of((char) 0xe194);
            case "cart-3":
                return Optional.of((char) 0xe195);
            case "cart-2":
                return Optional.of((char) 0xe196);
            case "bus-public-transport":
                return Optional.of((char) 0xe197);
            case "burglary":
                return Optional.of((char) 0xe198);
            case "building":
                return Optional.of((char) 0xe199);
            case "border":
                return Optional.of((char) 0xe19a);
            case "bookmark":
                return Optional.of((char) 0xe19b);
            case "badge":
                return Optional.of((char) 0xe19c);
            case "attachment-audio":
                return Optional.of((char) 0xe19d);
            case "attachment-video":
                return Optional.of((char) 0xe19e);
            case "attachment-html":
                return Optional.of((char) 0xe19f);
            case "attachment-photo":
                return Optional.of((char) 0xe1a0);
            case "attachment-e-pub":
                return Optional.of((char) 0xe1a1);
            case "attachment-zip-file":
                return Optional.of((char) 0xe1a2);
            case "attachment-text-file":
                return Optional.of((char) 0xe1a3);
            case "add-equipment":
                return Optional.of((char) 0xe1a4);
            case "add-activity":
                return Optional.of((char) 0xe1a5);
            case "activity-individual":
                return Optional.of((char) 0xe1a6);
            case "activity-2":
                return Optional.of((char) 0xe1a7);
            case "add-activity-2":
                return Optional.of((char) 0xe1a8);
            case "activity-items":
                return Optional.of((char) 0xe1a9);
            case "activity-assigned-to-goal":
                return Optional.of((char) 0xe1aa);
            case "status-completed":
                return Optional.of((char) 0xe1ab);
            case "status-positive":
                return Optional.of((char) 0xe1ab);
            case "status-error":
                return Optional.of((char) 0xe1ac);
            case "status-negative":
                return Optional.of((char) 0xe1ac);
            case "status-inactive":
                return Optional.of((char) 0xe1ad);
            case "status-in-process":
                return Optional.of((char) 0xe1ae);
            case "status-critical":
                return Optional.of((char) 0xe1ae);
            case "blank-tag-2":
                return Optional.of((char) 0xe1af);
            case "cart-full":
                return Optional.of((char) 0xe1b0);
            case "locate-me":
                return Optional.of((char) 0xe1b1);
            case "paging":
                return Optional.of((char) 0xe1b2);
            case "company-view":
                return Optional.of((char) 0xe1b3);
            case "document-text":
                return Optional.of((char) 0xe1b4);
            case "explorer":
                return Optional.of((char) 0xe1b5);
            case "personnel-view":
                return Optional.of((char) 0xe1b6);
            case "sorting-ranking":
                return Optional.of((char) 0xe1b7);
            case "drill-down":
                return Optional.of((char) 0xe1b8);
            case "drill-up":
                return Optional.of((char) 0xe1b9);
            case "vds-file":
                return Optional.of((char) 0xe1ba);
            case "sap-logo-shape":
                return Optional.of((char) 0xe1bb);
            case "folder-full":
                return Optional.of((char) 0xe1bc);
            case "system-exit":
                return Optional.of((char) 0xe1bd);
            case "system-exit-2":
                return Optional.of((char) 0xe1be);
            case "close-command-field":
                return Optional.of((char) 0xe1bf);
            case "open-command-field":
                return Optional.of((char) 0xe1c0);
            case "sys-enter-2":
                return Optional.of((char) 0xe1c1);
            case "sys-enter":
                return Optional.of((char) 0xe1c2);
            case "sys-help-2":
                return Optional.of((char) 0xe1c3);
            case "sys-help":
                return Optional.of((char) 0xe1c4);
            case "sys-back":
                return Optional.of((char) 0xe1c5);
            case "sys-back-2":
                return Optional.of((char) 0xe1c6);
            case "sys-cancel":
                return Optional.of((char) 0xe1c7);
            case "sys-cancel-2":
                return Optional.of((char) 0xe1c8);
            case "open-folder":
                return Optional.of((char) 0xe1c9);
            case "sys-find-next":
                return Optional.of((char) 0xe1ca);
            case "sys-find":
                return Optional.of((char) 0xe1cb);
            case "sys-monitor":
                return Optional.of((char) 0xe1cc);
            case "sys-prev-page":
                return Optional.of((char) 0xe1cd);
            case "sys-first-page":
                return Optional.of((char) 0xe1ce);
            case "sys-next-page":
                return Optional.of((char) 0xe1cf);
            case "sys-last-page":
                return Optional.of((char) 0xe1d0);
            case "generate-shortcut":
                return Optional.of((char) 0xe1d1);
            case "create-session":
                return Optional.of((char) 0xe1d2);
            case "display-more":
                return Optional.of((char) 0xe1d3);
            case "enter-more":
                return Optional.of((char) 0xe1d4);
            case "zoom-in":
                return Optional.of((char) 0xe1d5);
            case "zoom-out":
                return Optional.of((char) 0xe1d6);
            case "header":
                return Optional.of((char) 0xe1d7);
            case "detail-view":
                return Optional.of((char) 0xe1d8);
            case "show-edit":
                return Optional.of((char) 0xe1d8);
            case "collapse":
                return Optional.of((char) 0xe1d9);
            case "expand":
                return Optional.of((char) 0xe1da);
            case "positive":
                return Optional.of((char) 0xe1db);
            case "negative":
                return Optional.of((char) 0xe1dc);
            case "display":
                return Optional.of((char) 0xe1dd);
            case "menu2":
                return Optional.of((char) 0xe1de);
            case "redo":
                return Optional.of((char) 0xe1df);
            case "undo":
                return Optional.of((char) 0xe1e0);
            case "navigation-up-arrow":
                return Optional.of((char) 0xe1e1);
            case "navigation-down-arrow":
                return Optional.of((char) 0xe1e2);
            case "down":
                return Optional.of((char) 0xe1e3);
            case "up":
                return Optional.of((char) 0xe1e4);
            case "shelf":
                return Optional.of((char) 0xe1e5);
            case "background":
                return Optional.of((char) 0xe1e6);
            case "resize":
                return Optional.of((char) 0xe1e7);
            case "move":
                return Optional.of((char) 0xe1e8);
            case "show":
                return Optional.of((char) 0xe1e9);
            case "hide":
                return Optional.of((char) 0xe1ea);
            case "nav-back":
                return Optional.of((char) 0xe1eb);
            case "error":
                return Optional.of((char) 0xe1ec);
            case "slim-arrow-right":
                return Optional.of((char) 0xe1ed);
            case "slim-arrow-left":
                return Optional.of((char) 0xe1ee);
            case "slim-arrow-down":
                return Optional.of((char) 0xe1ef);
            case "slim-arrow-up":
                return Optional.of((char) 0xe1f0);
            case "forward":
                return Optional.of((char) 0xe1f1);
            case "overflow":
                return Optional.of((char) 0xe1f2);
            case "value-help":
                return Optional.of((char) 0xe1f3);
            case "multi-select":
                return Optional.of((char) 0xe1f4);
            case "exit-full-screen":
                return Optional.of((char) 0xe1f5);
            case "sys-add":
                return Optional.of((char) 0xe1f6);
            case "sys-minus":
                return Optional.of((char) 0xe1f7);
            case "dropdown":
                return Optional.of((char) 0xe1f8);
            case "expand-group":
                return Optional.of((char) 0xe1f9);
            case "collapse-group":
                return Optional.of((char) 0xe200);
            case "vertical-grip":
                return Optional.of((char) 0xe1fa);
            case "horizontal-grip":
                return Optional.of((char) 0xe1fb);
            case "sort-descending":
                return Optional.of((char) 0xe1fc);
            case "sort-ascending":
                return Optional.of((char) 0xe1fd);
            case "arrow-down":
                return Optional.of((char) 0xe1fe);
            case "legend":
                return Optional.of((char) 0xe1ff);
            case "message-warning":
                return Optional.of((char) 0xe201);
            case "message-information":
                return Optional.of((char) 0xe202);
            case "message-success":
                return Optional.of((char) 0xe203);
            case "restart":
                return Optional.of((char) 0xe204);
            case "stop":
                return Optional.of((char) 0xe205);
            case "add-process":
                return Optional.of((char) 0xe206);
            case "cancel-maintenance":
                return Optional.of((char) 0xe207);
            case "activate":
                return Optional.of((char) 0xe208);
            case "resize-horizontal":
                return Optional.of((char) 0xe209);
            case "resize-vertical":
                return Optional.of((char) 0xe20a);
            case "connected":
                return Optional.of((char) 0xe20b);
            case "disconnected":
                return Optional.of((char) 0xe20c);
            case "edit-outside":
                return Optional.of((char) 0xe20d);
            case "key":
                return Optional.of((char) 0xe20e);
            case "minimize":
                return Optional.of((char) 0xe20f);
            case "back-to-top":
                return Optional.of((char) 0xe210);
            case "hello-world":
                return Optional.of((char) 0xe211);
            case "outbox":
                return Optional.of((char) 0xe212);
            case "donut-chart":
                return Optional.of((char) 0xe213);
            case "heatmap-chart":
                return Optional.of((char) 0xe214);
            case "horizontal-bullet-chart":
                return Optional.of((char) 0xe215);
            case "vertical-bullet-chart":
                return Optional.of((char) 0xe216);
            case "call":
                return Optional.of((char) 0xe217);
            case "download-from-cloud":
                return Optional.of((char) 0xe218);
            case "upload-to-cloud":
                return Optional.of((char) 0xe219);
            case "jam":
                return Optional.of((char) 0xe21a);
            case "sap-ui5":
                return Optional.of((char) 0xe21b);
            case "message-popup":
                return Optional.of((char) 0xe21c);
            case "cloud":
                return Optional.of((char) 0xe21d);
            case "horizontal-waterfall-chart":
                return Optional.of((char) 0xe21e);
            case "vertical-waterfall-chart":
                return Optional.of((char) 0xe21f);
            case "broken-link":
                return Optional.of((char) 0xe220);
            case "headset":
                return Optional.of((char) 0xe221);
            case "thumb-up":
                return Optional.of((char) 0xe222);
            case "thumb-down":
                return Optional.of((char) 0xe223);
            case "multiselect-all":
                return Optional.of((char) 0xe224);
            case "multiselect-none":
                return Optional.of((char) 0xe225);
            case "scissors":
                return Optional.of((char) 0xe226);
            case "sound":
                return Optional.of((char) 0xe227);
            case "sound-loud":
                return Optional.of((char) 0xe228);
            case "sound-off":
                return Optional.of((char) 0xe229);
            case "date-time":
                return Optional.of((char) 0xe22a);
            case "user-settings":
                return Optional.of((char) 0xe22b);
            case "key-user-settings":
                return Optional.of((char) 0xe22c);
            case "developer-settings":
                return Optional.of((char) 0xe22d);
            case "text-formatting":
                return Optional.of((char) 0xe22e);
            case "bold-text":
                return Optional.of((char) 0xe22f);
            case "italic-text":
                return Optional.of((char) 0xe230);
            case "underline-text":
                return Optional.of((char) 0xe231);
            case "text-align-justified":
                return Optional.of((char) 0xe232);
            case "text-align-left":
                return Optional.of((char) 0xe233);
            case "text-align-center":
                return Optional.of((char) 0xe234);
            case "text-align-right":
                return Optional.of((char) 0xe235);
            case "bullet-text":
                return Optional.of((char) 0xe236);
            case "numbered-text":
                return Optional.of((char) 0xe237);
            case "co":
                return Optional.of((char) 0xe238);
            case "ui-notifications":
                return Optional.of((char) 0xe239);
            case "bell":
                return Optional.of((char) 0xe23a);
            case "cancel-share":
                return Optional.of((char) 0xe23b);
            case "write-new-document":
                return Optional.of((char) 0xe23c);
            case "write-new":
                return Optional.of((char) 0xe23d);
            case "cancel":
                return Optional.of((char) 0xe23e);
            case "screen-split-one":
                return Optional.of((char) 0xe23f);
            case "screen-split-two":
                return Optional.of((char) 0xe240);
            case "screen-split-three":
                return Optional.of((char) 0xe241);
            case "customize":
                return Optional.of((char) 0xe242);
            case "user-edit":
                return Optional.of((char) 0xe243);
            case "source-code":
                return Optional.of((char) 0xe244);
            case "copy":
                return Optional.of((char) 0xe245);
            case "paste":
                return Optional.of((char) 0xe246);
            case "line-chart-time-axis":
                return Optional.of((char) 0xe247);
            case "clear-filter":
                return Optional.of((char) 0xe248);
            case "reset":
                return Optional.of((char) 0xe249);
            case "trend-up":
                return Optional.of((char) 0xe24a);
            case "trend-down":
                return Optional.of((char) 0xe24b);
            case "cursor-arrow":
                return Optional.of((char) 0xe24c);
            case "add-document":
                return Optional.of((char) 0xe24d);
            case "create-form":
                return Optional.of((char) 0xe24e);
            case "resize-corner":
                return Optional.of((char) 0xe24f);
            case "chevron-phase":
                return Optional.of((char) 0xe250);
            case "chevron-phase-2":
                return Optional.of((char) 0xe251);
            case "rhombus-milestone":
                return Optional.of((char) 0xe252);
            case "rhombus-milestone-2":
                return Optional.of((char) 0xe253);
            case "circle-task":
                return Optional.of((char) 0xe254);
            case "circle-task-2":
                return Optional.of((char) 0xe255);
            case "project-definition-triangle":
                return Optional.of((char) 0xe256);
            case "project-definition-triangle-2":
                return Optional.of((char) 0xe257);
            case "master-task-triangle":
                return Optional.of((char) 0xe258);
            case "master-task-triangle-2":
                return Optional.of((char) 0xe259);
            case "program-triangles":
                return Optional.of((char) 0xe25a);
            case "program-triangles-2":
                return Optional.of((char) 0xe25b);
            case "mirrored-task-circle":
                return Optional.of((char) 0xe25c);
            case "mirrored-task-circle-2":
                return Optional.of((char) 0xe25d);
            case "checklist-item":
                return Optional.of((char) 0xe25e);
            case "checklist-item-2":
                return Optional.of((char) 0xe25f);
            case "checklist":
                return Optional.of((char) 0xe260);
            case "checklist-2":
                return Optional.of((char) 0xe261);
            case "chart-table-view":
                return Optional.of((char) 0xe262);
            case "filter-analytics":
                return Optional.of((char) 0xe263);
            case "filter-facets":
                return Optional.of((char) 0xe264);
            case "filter-fields":
                return Optional.of((char) 0xe265);
            case "indent":
                return Optional.of((char) 0xe266);
            case "outdent":
                return Optional.of((char) 0xe267);
            case "heading1":
                return Optional.of((char) 0xe268);
            case "heading2":
                return Optional.of((char) 0xe269);
            case "heading3":
                return Optional.of((char) 0xe26a);
            case "decrease-line-height":
                return Optional.of((char) 0xe26b);
            case "increase-line-height":
                return Optional.of((char) 0xe26c);
            case "fx":
                return Optional.of((char) 0xe26d);
            case "add-folder":
                return Optional.of((char) 0xe26e);
            case "away":
                return Optional.of((char) 0xe26f);
            case "busy":
                return Optional.of((char) 0xe270);
            case "appear-offline":
                return Optional.of((char) 0xe271);
            case "blur":
                return Optional.of((char) 0xe272);
            case "pixelate":
                return Optional.of((char) 0xe273);
            case "horizontal-combination-chart":
                return Optional.of((char) 0xe274);
            case "add-employee":
                return Optional.of((char) 0xe275);
            case "text-color":
                return Optional.of((char) 0xe276);
            case "browse-folder":
                return Optional.of((char) 0xe277);
            case "primary-key":
                return Optional.of((char) 0xe278);
            case "two-keys":
                return Optional.of((char) 0xe279);
            case "strikethrough":
                return Optional.of((char) 0xe27a);
            case "text":
                return Optional.of((char) 0xe27b);
            case "responsive":
                return Optional.of((char) 0xe27c);
            case "desktop-mobile":
                return Optional.of((char) 0xe27d);
            case "table-row":
                return Optional.of((char) 0xe27e);
            case "table-column":
                return Optional.of((char) 0xe27f);
            case "validate":
                return Optional.of((char) 0xe280);
            case "keyboard-and-mouse":
                return Optional.of((char) 0xe281);
            case "touch":
                return Optional.of((char) 0xe282);
            case "expand-all":
                return Optional.of((char) 0xe283);
            case "collapse-all":
                return Optional.of((char) 0xe284);
        }

        return Optional.empty();
    }
}
