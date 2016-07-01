/**
 * Copyright (C) 2012 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.spinetrak.ninja.conf;

import net.spinetrak.ninja.controllers.ApplicationController;
import ninja.AssetsController;
import ninja.Router;
import ninja.application.ApplicationRoutes;

public class Routes implements ApplicationRoutes
{

  @Override
  public void init(Router router)
  {

    router.GET().route("/").with(ApplicationController.class, "index");
    router.GET().route("/track/new").with(ApplicationController.class, "create");
    router.POST().route("/track/new").with(ApplicationController.class, "postGPXParams");
    router.GET().route("/track/{id}").with(ApplicationController.class, "view");

    ///////////////////////////////////////////////////////////////////////
    // Assets (pictures / javascript)
    ///////////////////////////////////////////////////////////////////////
    router.GET().route("/assets/webjars/{fileName: .*}").
      with(AssetsController.class, "serveWebJars");
    router.GET().route("/assets/{fileName: .*}").
      with(AssetsController.class, "serveStatic");

    ///////////////////////////////////////////////////////////////////////
    // Index / Catchall shows index page
    ///////////////////////////////////////////////////////////////////////
    router.GET().route("/favicon.ico").with(AssetsController.class, "serveStatic");
    router.GET().route("/.*").with(ApplicationController.class, "index");
  }

}
