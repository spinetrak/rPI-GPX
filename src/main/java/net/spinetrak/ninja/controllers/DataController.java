package net.spinetrak.ninja.controllers;

import com.google.inject.Singleton;
import net.spinetrak.data.Location;
import net.spinetrak.data.Power;
import ninja.Result;
import ninja.Results;

@Singleton
public class DataController
{
  public Result locationLatitude()
  {
    final Location location = new Location();
    location.setLatitude(53.4456);
    return Results.json().render(location);
  }

  public Result locationLongitude()
  {
    final Location location = new Location();
    location.setLongitude(9.3453);
    return Results.json().render(location);
  }

  public Result powerSource()
  {
    final Power power = new Power();
    return Results.json().render(power);
  }

  public Result powerVoltage()
  {
    final Power power = new Power();
    return Results.json().render(power);
  }
}
