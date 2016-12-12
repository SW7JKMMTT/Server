CREATE INDEX uk__vehicledata_route_id ON public.vehicledata (route_id);

CREATE INDEX uk__vehicledatapoint__vehicledata_id ON public.vehicledatapoint (vehicledata_id);

CREATE INDEX uk__waypoint__route_id ON public.waypoint (route_id);

CREATE INDEX uk__permission__user_id ON public.permission (user_id);
