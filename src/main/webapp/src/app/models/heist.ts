import {HeistSkill} from "./heist-skill";

export class Heist {

  name?: string;
  location?: string;
  startTime?: string | null;
  endTime?: string | null;
  skills?: HeistSkill[];
  status?: string;

}
